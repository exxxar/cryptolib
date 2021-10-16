package com.core.cryptolib;

import com.core.cryptolib.components.ConnectionObject;
import com.core.cryptolib.components.Connections;
import com.core.cryptolib.components.Settings;
import com.core.cryptolib.enums.InfoRequestType;
import com.core.cryptolib.factories.RequestPrepareFactory;
import com.core.cryptolib.forms.EncryptedDataForm;
import com.core.cryptolib.forms.HandlerResultForm;
import com.core.cryptolib.forms.TransferDataForm;
import com.core.cryptolib.forms.TransferForm;
import com.core.cryptolib.services.des.enums.Algorithm;
import com.core.cryptolib.services.des.enums.ChipperMode;
import com.core.cryptolib.services.des.enums.DesPaddingMode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.log4j.Logger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UserPayloadServiceForServer {

    Settings settings;

    Connections connections;

    RequestPrepareFactory requestPrepareFactory;

    EncryptService desApp;

    CryptoLoggerService logger;

    public UserPayloadServiceForServer(Connections connections, Settings settings, CryptoLoggerService logger) {

        this.desApp = new EncryptService();

        this.settings = settings;

        this.connections = connections;

        if (this.settings != null) {

            boolean isDebug = Boolean.parseBoolean((String) settings.get("logger_debug").getValue());

            this.logger = logger;

            this.logger.setDebugMode(isDebug);

        }

        this.requestPrepareFactory = new RequestPrepareFactory(this.settings);

    }

    public UserPayloadServiceForServer(Settings settings, CryptoLoggerService logger) {

        this.desApp = new EncryptService();

        this.settings = settings;

        this.connections = null;

        if (this.settings != null) {

            boolean isDebug = Boolean.parseBoolean((String) settings.get("logger_debug").getValue());

            this.logger = logger;

            this.logger.setDebugMode(isDebug);

        }

        this.requestPrepareFactory = new RequestPrepareFactory(this.settings);

    }

    public Connections getConnections() {
        return this.connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public TransferDataForm getTrustedDevicePublicId(String ownPublicDeviceId) throws ParseException, UnsupportedEncodingException {

        TransferDataForm tdf = new TransferDataForm();
        tdf.setType(InfoRequestType.onceEncryptedRequest.getValue());
        tdf.setData(ownPublicDeviceId);

        return tdf;

    }

    public TransferDataForm onceEncryptedRequest(String ownPrivateDeviceId, String ownPublicDeviceId, String trustedDeviceActualKey) throws ParseException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] ownActualKey = Base64.getDecoder().decode(
                trustedDeviceActualKey
        );

        JSONObject encryptedDataExchangeRequest = new JSONObject();
        encryptedDataExchangeRequest.put("trustedDevicePrivateId", ownPrivateDeviceId);

        byte[] IV = desApp.getIV(8);
        SecretKey key = EncryptService.getKeyFromBytes(ownActualKey);
        byte[] encryptedSenderData = desApp.encryptToByte(encryptedDataExchangeRequest
                .toJSONString().getBytes(),
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        byte[] resultEncrypt = new byte[IV.length + encryptedSenderData.length];
        System.arraycopy(IV, 0, resultEncrypt, 0, IV.length);
        System.arraycopy(encryptedSenderData, 0, resultEncrypt, IV.length, encryptedSenderData.length);

        JSONObject encodedJSON = new JSONObject();
        encodedJSON.put("senderTrustedDevicePublicId", ownPublicDeviceId);
        encodedJSON.put("encryptedDataExchangeRequest", Base64
                .getEncoder()
                .encodeToString(resultEncrypt));

        TransferDataForm outcomingTransferDataForm = new TransferDataForm();
        outcomingTransferDataForm.setType(InfoRequestType.onceEncryptedRequest.getValue());
        outcomingTransferDataForm.setData(encodedJSON.toJSONString());

        return outcomingTransferDataForm;

    }

    public TransferDataForm twiceEncryptedRequest(
            String recipientTrustedDevicePrivateId,
            String recipientTrustedDeviceActualKey,
            String recipientTrustedDevicePublicId,
            TransferDataForm tdf) throws ParseException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, InvalidKeySpecException {

        if (tdf.getType() != InfoRequestType.onceEncryptedRequest.getValue()) {
            return denailRequest();
        }

        JSONParser parser = new JSONParser();
        JSONObject encryptedDataExchangeRequest = (JSONObject) parser.parse(tdf.getData());

        JSONObject restoredData = (JSONObject) parser.parse(
                new String(Base64.getDecoder().decode(
                        (String) encryptedDataExchangeRequest.get("data")
                )));

        String dataEncryptedDataExchangeRequest = (String) restoredData.get("encryptedDataExchangeRequest");

        JSONObject data = new JSONObject();
        data.put("senderTrustedDevicePublicId", (String) restoredData.get("senderTrustedDevicePublicId"));
        data.put("recipientTrustedDevicePublicId", recipientTrustedDevicePublicId);

        logger.info("twiceEncryptedRequest senderTrustedDevicePublicId",
                (String) restoredData.get("senderTrustedDevicePublicId"));

        byte[] ownActualKey = Base64.getDecoder().decode(
                recipientTrustedDeviceActualKey
        );

        JSONObject newEncryptObject = new JSONObject();
        newEncryptObject.put("encryptedDataExchangeRequest", dataEncryptedDataExchangeRequest);
        newEncryptObject.put("trustedDevicePrivateId", recipientTrustedDevicePrivateId);

        logger.info("twiceEncryptedRequest trustedDevicePrivateId",
                recipientTrustedDevicePrivateId);

        logger.info("twiceEncryptedRequest (before) encryptedDataExchangeRequest",
                dataEncryptedDataExchangeRequest);

        byte[] resultEncrypt = null;
        try {
            byte[] IV = desApp.getIV(8);
            SecretKey key = EncryptService.getKeyFromBytes(ownActualKey);
            byte[] encryptedSenderData = desApp.encryptToByte(newEncryptObject.toJSONString().getBytes(),
                    key,
                    EncryptService.getAlgo(Algorithm.DES.getValue(),
                            ChipperMode.CBC.getValue(),
                            DesPaddingMode.PKCS7_PADDING.getValue()),
                    IV);

            resultEncrypt = new byte[IV.length + encryptedSenderData.length];
            System.arraycopy(IV, 0, resultEncrypt, 0, IV.length);
            System.arraycopy(encryptedSenderData, 0, resultEncrypt, IV.length, encryptedSenderData.length);

        } catch (Exception ex) {
            logger.error("twiceEncryptedRequest exception",
                    ex.getMessage());
            return denailRequest();
        }
        data.put("encryptedDataExchangeRequest", Base64
                .getEncoder()
                .encodeToString(resultEncrypt));

        logger.info("twiceEncryptedRequest (after) encryptedDataExchangeRequest=>",
                Base64
                        .getEncoder()
                        .encodeToString(resultEncrypt)
        );

        TransferDataForm outcomingTransferDataForm = new TransferDataForm();
        outcomingTransferDataForm.setType(InfoRequestType.twiceEncryptedRequest.getValue());
        outcomingTransferDataForm.setData(data.toJSONString());

        return outcomingTransferDataForm;

    }

    public TransferDataForm twiceEncryptedPermission(String trustedDeviceActualKey, TransferDataForm incomingTransferDataForm) throws ParseException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, InvalidKeySpecException {

        if (incomingTransferDataForm.getType() != InfoRequestType.twiceEncryptedPermission.getValue()) {
            return denailRequest();
        }

        byte[] ownActualKey = Base64.getDecoder().decode(
                trustedDeviceActualKey
        );

        byte[] d2 = incomingTransferDataForm.getDataInBytes();

        byte[] IV = Arrays.copyOfRange(d2, 0, 8);
        byte[] encoded = Arrays.copyOfRange(d2, 8, d2.length);

        SecretKey key = EncryptService.getKeyFromBytes(ownActualKey);

        byte[] decryptedData = desApp.decryptToByte(encoded,
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        JSONParser parser = new JSONParser();
        JSONObject resultDecryptedJSON = (JSONObject) parser.parse(new String(decryptedData));

        ConnectionObject co = connections.get((String) resultDecryptedJSON.get("senderTrustedDevicePublicId"));

        if (co != null) {
            co.setDeviceOldKey(co.getDeviceActualKey());
            co.setDeviceActualKey((String) resultDecryptedJSON.get("senderTrustedDeviceActualKey"));
            co.setMicroserviceOldKey(co.getMicroserviceActualKey());
            co.setMicroserviceActualKey((String) resultDecryptedJSON.get("recipientTrustedDeviceActualKey"));
        } else {
            co = new ConnectionObject(
                    (String) resultDecryptedJSON.get("senderTrustedDevicePublicId"),
                    (String) resultDecryptedJSON.get("senderTrustedDeviceActualKey"),
                    (String) resultDecryptedJSON.get("recipientTrustedDeviceActualKey")
            );
        }

        connections.put(co);

        ///для единичного подключения оставляем
        settings.put("serverTrustedDeviceOldKey", settings.get("serverTrustedDeviceActualKey").getValue());
        settings.put("serverTrustedDeviceActualKey", (String) resultDecryptedJSON.get("recipientTrustedDeviceActualKey"));

        settings.put("senderTrustedDevicePublicId", (String) resultDecryptedJSON.get("senderTrustedDevicePublicId"));
        settings.put("senderTrustedDeviceActualKey", (String) resultDecryptedJSON.get("senderTrustedDeviceActualKey"));

        logger.info("twiceEncryptedPermission senderTrustedDevicePublicId",
                (String) resultDecryptedJSON.get("senderTrustedDevicePublicId")
        );

        incomingTransferDataForm.setType(InfoRequestType.onceEncryptedPermission.getValue());
        incomingTransferDataForm.setDataBase64((String) resultDecryptedJSON.get("encryptedDataExchangePermission"));

        return incomingTransferDataForm;
    }

    public TransferDataForm denailRequest() throws UnsupportedEncodingException {

        TransferDataForm tdf = new TransferDataForm();

        tdf.setType(InfoRequestType.denial.getValue());
        tdf.setData("");

        return tdf;

    }

    public TransferDataForm dataRequestMultiply(String devicePublicId, TransferDataForm incomingTransferDataForm) throws ParseException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        try {
            logger.info("dataRequest");
            String serverTrustedDeviceActualKey = connections.get(devicePublicId).getDeviceActualKey();

            logger.info("serverTrustedDeviceActualKey",
                    serverTrustedDeviceActualKey);

            byte[] senderTrustedDeviceActualKey = Base64.getDecoder().decode(serverTrustedDeviceActualKey.getBytes());

            logger.info("incomingTransferDataForm.getData(new1)",
                    incomingTransferDataForm.getDataInBase64());
            logger.info("incomingTransferDataForm.getData(new2)",
                    incomingTransferDataForm.getData());

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(incomingTransferDataForm.getData());

            logger.info("data obj.get",
                    (String) obj.get("data"));

            TransferDataForm tdf = new TransferDataForm();
            tdf.setData((String) obj.get("data"));

            logger.info("dataRequest create TransferDataForm");
            tdf.setType(Integer.parseInt(obj.get("type").toString()));

            if (tdf.getType() != InfoRequestType.encryptedData.getValue()) {
                return denailRequest();
            }

            logger.info("dataRequest type==encryptedData" + tdf.getData());

            byte[] d2 = tdf.getDataInBytes();
            logger.info("dataRequest get TransferDataForm data in bytes");
            byte[] IV = Arrays.copyOfRange(d2, 0, 8);

            byte[] encoded = Arrays.copyOfRange(d2, 8, d2.length);

            SecretKey key = EncryptService.getKeyFromBytes(senderTrustedDeviceActualKey);

            byte[] decryptedData = desApp.decryptToByte(encoded,
                    key,
                    EncryptService.getAlgo(Algorithm.DES.getValue(),
                            ChipperMode.CBC.getValue(),
                            DesPaddingMode.PKCS7_PADDING.getValue()),
                    IV);

            logger.info("dataRequest get success decrypt data", new String(decryptedData));
            incomingTransferDataForm.setType(InfoRequestType.data.getValue());

            incomingTransferDataForm.setData(new String(decryptedData));

        } catch (UnsupportedEncodingException | NumberFormatException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ParseException e) {
            logger.error("dataRequest error decrypt data");
            return denailRequest();
        }

        return incomingTransferDataForm;
    }

    public TransferDataForm dataRequestSingle(TransferDataForm incomingTransferDataForm) throws ParseException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        try {

            byte[] senderTrustedDeviceActualKey = Base64.getDecoder().decode(
                    (String) settings.get("senderTrustedDeviceActualKey").getValue()
            );

            logger.info("incomingTransferDataForm.getData(new1)",
                    incomingTransferDataForm.getDataInBase64());
            logger.info("incomingTransferDataForm.getData(new2)",
                    incomingTransferDataForm.getData());

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(incomingTransferDataForm.getData());

            TransferDataForm tdf = new TransferDataForm();
            tdf.setData((String) obj.get("data"));

            logger.info("dataRequest create TransferDataForm");
            tdf.setType(Integer.parseInt(obj.get("type").toString()));

            if (tdf.getType() != InfoRequestType.encryptedData.getValue()) {
                return denailRequest();
            }

            byte[] d2 = tdf.getDataInBytes();
            byte[] IV = Arrays.copyOfRange(d2, 0, 8);

            byte[] encoded = Arrays.copyOfRange(d2, 8, d2.length);

            SecretKey key = EncryptService.getKeyFromBytes(senderTrustedDeviceActualKey);

            byte[] decryptedData = desApp.decryptToByte(encoded,
                    key,
                    EncryptService.getAlgo(Algorithm.DES.getValue(),
                            ChipperMode.CBC.getValue(),
                            DesPaddingMode.PKCS7_PADDING.getValue()),
                    IV);

            logger.info("dataRequest get success decrypt data", new String(decryptedData));
            incomingTransferDataForm.setType(InfoRequestType.data.getValue());

            incomingTransferDataForm.setData(new String(decryptedData));

        } catch (UnsupportedEncodingException | NumberFormatException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ParseException e) {
            logger.error("dataRequest error decrypt data");
            return denailRequest();
        }

        return incomingTransferDataForm;
    }

    public TransferDataForm encryptedDataRequestSingle(
            EncryptedDataForm incomingTransferDataForm) throws ParseException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        if (incomingTransferDataForm.getType() != InfoRequestType.data.getValue()) {
            return denailRequest();
        }

        byte[] senderTrustedDeviceActualKey = Base64.getDecoder().decode(
                (String) settings.get("senderTrustedDeviceActualKey").getValue()
        );

        JSONObject forEncryptJSON = new JSONObject();

        forEncryptJSON.put("trustedDeviceData", incomingTransferDataForm.getTrustedDeviceData());
        forEncryptJSON.put("userData", incomingTransferDataForm.getUserData());

        byte[] IV = desApp.getIV(8);
        SecretKey key = EncryptService.getKeyFromBytes(senderTrustedDeviceActualKey);
        byte[] encryptedSenderData = desApp.encryptToByte(forEncryptJSON.toJSONString().getBytes(),
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        byte[] resultEncrypt = new byte[IV.length + encryptedSenderData.length];
        System.arraycopy(IV, 0, resultEncrypt, 0, IV.length);
        System.arraycopy(encryptedSenderData, 0, resultEncrypt, IV.length, encryptedSenderData.length);

        TransferDataForm outgouingTrnasferDataForm = new TransferDataForm();
        outgouingTrnasferDataForm.setType(InfoRequestType.encryptedData.getValue());
        outgouingTrnasferDataForm.setData(resultEncrypt);

        return outgouingTrnasferDataForm;
    }

    public TransferDataForm encryptedDataRequestMultiply(
            String devicePublicId,
            EncryptedDataForm incomingTransferDataForm) throws ParseException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        if (incomingTransferDataForm.getType() != InfoRequestType.data.getValue()) {
            return denailRequest();
        }

        byte[] senderTrustedDeviceActualKey = Base64.getDecoder().decode(
                connections.get(devicePublicId).getDeviceActualKey()
        );

        JSONObject forEncryptJSON = new JSONObject();

        forEncryptJSON.put("trustedDeviceData", incomingTransferDataForm.getTrustedDeviceData());
        forEncryptJSON.put("userData", incomingTransferDataForm.getUserData());

        byte[] IV = desApp.getIV(8);
        SecretKey key = EncryptService.getKeyFromBytes(senderTrustedDeviceActualKey);
        byte[] encryptedSenderData = desApp.encryptToByte(forEncryptJSON.toJSONString().getBytes(),
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        byte[] resultEncrypt = new byte[IV.length + encryptedSenderData.length];
        System.arraycopy(IV, 0, resultEncrypt, 0, IV.length);
        System.arraycopy(encryptedSenderData, 0, resultEncrypt, IV.length, encryptedSenderData.length);

        TransferDataForm outgouingTrnasferDataForm = new TransferDataForm();
        outgouingTrnasferDataForm.setType(InfoRequestType.encryptedData.getValue());
        outgouingTrnasferDataForm.setData(resultEncrypt);

        return outgouingTrnasferDataForm;
    }

    public HandlerResultForm handlerMultiply(String devicePublicId, TransferForm transfer) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, IOException {

        ConnectionObject co = connections.get(devicePublicId);

        String ownTrustedDevicePrivateId = (String) settings
                .get("serverTrustedDevicePrivateId")
                .getValue();

        String ownTrustedDeviceActualKey
                = co.getMicroserviceActualKey();

        if (ownTrustedDeviceActualKey.isBlank() || ownTrustedDeviceActualKey.isEmpty()) {
            ownTrustedDeviceActualKey = (String) settings
                    .get("serverTrustedDeviceActualKey")
                    .getValue();
        }

        String ownTrustedDevicePublicId = (String) settings
                .get("serverTrustedDevicePublicId")
                .getValue();

        String cryptograpicURL = ((String) settings
                .get("serverToCryptograpicUrl").getValue())
                .concat("/trusted_devices/reencrypt");

        String serverUserId = (String) settings
                .get("serverUserId")
                .getValue();

        logger.info(String.format("Handler start!", "ownTrustedDevicePrivateId=%s\nownTrustedDeviceActualKey=%s\nownTrustedDevicePublicId=%s\ncryptograpicURL=%s\nserverUserId=%s ",
                ownTrustedDevicePrivateId,
                ownTrustedDeviceActualKey,
                ownTrustedDevicePublicId,
                cryptograpicURL,
                serverUserId
        ));

        HandlerResultForm result = new HandlerResultForm(transfer);

        logger.info("transfer type", "" + transfer.getDataType());

        if (transfer.getSenderUserId().equals(serverUserId) && transfer.getDataType() == 8) {

            logger.info("sender_user_id==server_user_id&transfer date type=8");

            try {
                TransferDataForm tdf = dataRequestMultiply(devicePublicId, transfer.getTransferDataForm());

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(tdf.getData());

                String tmp = new String(Base64.getDecoder().decode((String) obj.get("userData")));
                result.setData(tmp);

                logger.info("decrypted data=>" + tmp);
                return result;
            } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ParseException ex) {
                logger.error("try decrypt error", "serverUserId" + serverUserId
                        + "  transfer data type=>" + transfer.getDataType()
                        + " exception=>" + ex.getMessage());
                result.setData(transfer.getData());
                return result;
            }
        }

        if (transfer.getTransferType() == 1 || transfer.getRecipientUserId().equals(serverUserId)) {
            logger.info("transfer type==1||transfer recipient_user_id==server_user_id");
            result.setData(transfer.getData());
            return result;

        }
        String data = "", payload = "";

        transfer.setCreateDateTime(LocalDateTime.now());
        transfer.setStatusType(1);

        switch (transfer.getDataType()) {

            case 1:

                logger.info("transfer data type = 1");

                TransferDataForm tdf = new TransferDataForm(transfer.getTransferDataForm());

                tdf = twiceEncryptedRequest(
                        ownTrustedDevicePrivateId,
                        ownTrustedDeviceActualKey,
                        ownTrustedDevicePublicId,
                        tdf
                );

                logger.info("Make twiceEncryptedRequest ", tdf.getData());

                TransferForm tf = new TransferForm();
                tf.setData(tdf.toBase64JSON());
                tf.setRecipientUserId(serverUserId);
                tf.setSenderUserId(serverUserId);

                logger
                        .info(String.format("url=>%s", cryptograpicURL));

                JSONObject obj = requestPrepareFactory.jsonPost(
                        cryptograpicURL,
                        Optional.of("0.0.3"),
                        tf.toJSON()
                );

                logger.info(
                        "Make Cryptographic request ", obj.toJSONString());

                JSONParser parser = new JSONParser();
                JSONObject resultData = (JSONObject) parser.parse(new String(Base64
                        .getDecoder()
                        .decode(obj
                                .get("data")
                                .toString())));

                tdf.setDataBase64(
                        (String) resultData.get("data"));
                tdf.setType(Integer.parseInt(resultData.get("type").toString()));

                tdf = twiceEncryptedPermission(ownTrustedDeviceActualKey, tdf);

                logger.info(
                        "twiceEncryptedPermission=>", tdf.toJSON().toJSONString());

                TransferForm responseTransfer = new TransferForm();

                responseTransfer.setData(tdf.toBase64JSON());
                responseTransfer.setStatusType(0);

                responseTransfer.setRecipientUserId(transfer.getSenderUserId());
                responseTransfer.setSenderUserId(transfer.getRecipientUserId());
                responseTransfer.setCreateDateTime(LocalDateTime.now());

                result.setOutgoingTransfer(responseTransfer);

                logger.info(
                        "Save transfers(old)", transfer.toJSON().toJSONString());
                logger.info(
                        "Save transfers(new)", responseTransfer.toJSON().toJSONString());

                break;
            case 6:
                logger.info("transfer data type = 6");
                data = transfer.getTransferDataForm().getData();
                logger.info("data", data);
                break;
            case 7:

                logger.info("transfer data type = 7");
                payload = transfer.getTransferDataForm().getData();
                logger.info("data", data);
                break;
            case 8:
                logger.info("transfer data type = 8");
                tdf = dataRequestMultiply(devicePublicId, transfer.getTransferDataForm());

                result.setOutgoingTransfer(transfer);

                parser = new JSONParser();
                obj = (JSONObject) parser.parse(tdf.getData());

                payload = new String(Base64.getDecoder().decode((String) obj.get("userData")));
                logger.info("data", data);
                break;

            default:
                data = "";
                payload = "";
        }

        result.setData(data);
        result.setPayload(payload);

        return result;

    }

    public HandlerResultForm handlerSingle(TransferForm transfer) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, IOException {

        logger.info("handlerSingle step 0");
        String ownTrustedDevicePrivateId = (String) settings
                .get("serverTrustedDevicePrivateId")
                .getValue();

        String ownTrustedDeviceActualKey = (String) settings
                .get("serverTrustedDeviceActualKey")
                .getValue();

        String ownTrustedDevicePublicId = (String) settings
                .get("serverTrustedDevicePublicId")
                .getValue();

        String cryptograpicURL = ((String) settings
                .get("serverToCryptograpicUrl").getValue())
                .concat("/trusted_devices/reencrypt");

        String serverUserId = (String) settings
                .get("serverUserId")
                .getValue();

        HandlerResultForm result = new HandlerResultForm(transfer);
        logger.info("handlerSingle step 1");
        if (transfer.getSenderUserId().equals(serverUserId) && transfer.getDataType() == 8) {

            try {
                TransferDataForm tdf = dataRequestSingle(transfer.getTransferDataForm());

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(tdf.getData());

                result.setData(new String(Base64.getDecoder().decode((String) obj.get("userData"))));
                return result;
            } catch (Exception ex) {
                result.setData(transfer.getData());
                return result;
            }
        }

        logger.info("handlerSingle step 2");
        if (transfer.getTransferType() == 1 || transfer.getRecipientUserId().equals(serverUserId)) {
            logger.info("transfer is="+transfer.toJSON().toJSONString());
            logger.info("serverUserId is="+serverUserId);
            result.setData(transfer.getData());
            return result;

        }
           String data = "", payload = "";

        transfer.setCreateDateTime(LocalDateTime.now());
        transfer.setStatusType(1);

        switch (transfer.getDataType()) {

            case 1:

                logger.info("transfer data type = 1");

                TransferDataForm tdf = new TransferDataForm(transfer.getTransferDataForm());

                tdf = twiceEncryptedRequest(
                        ownTrustedDevicePrivateId,
                        ownTrustedDeviceActualKey,
                        ownTrustedDevicePublicId,
                        tdf
                );

                logger.info("Make twiceEncryptedRequest ", tdf.getData());

                TransferForm tf = new TransferForm();
                tf.setData(tdf.toBase64JSON());
                tf.setRecipientUserId(serverUserId);
                tf.setSenderUserId(serverUserId);

                logger
                        .info(String.format("url=>%s", cryptograpicURL));

                JSONObject obj = requestPrepareFactory.jsonPost(
                        cryptograpicURL,
                        Optional.of("0.0.3"),
                        tf.toJSON()
                );

                logger.info(
                        "Make Cryptographic request ", obj.toJSONString());

                JSONParser parser = new JSONParser();
                JSONObject resultData = (JSONObject) parser.parse(new String(Base64
                        .getDecoder()
                        .decode(obj
                                .get("data")
                                .toString())));

                tdf.setDataBase64(
                        (String) resultData.get("data"));
                tdf.setType(Integer.parseInt(resultData.get("type").toString()));

                tdf = twiceEncryptedPermission(ownTrustedDeviceActualKey, tdf);

                logger.info(
                        "twiceEncryptedPermission=>", tdf.toJSON().toJSONString());

                TransferForm responseTransfer = new TransferForm();

                responseTransfer.setData(tdf.toBase64JSON());
                responseTransfer.setStatusType(0);

                responseTransfer.setRecipientUserId(transfer.getSenderUserId());
                responseTransfer.setSenderUserId(transfer.getRecipientUserId());
                responseTransfer.setCreateDateTime(LocalDateTime.now());

                result.setOutgoingTransfer(responseTransfer);

                logger.info(
                        "Save transfers(old)", transfer.toJSON().toJSONString());
                logger.info(
                        "Save transfers(new)", responseTransfer.toJSON().toJSONString());

                break;
            case 6:
                logger.info("transfer data type = 6");
                data = transfer.getTransferDataForm().getData();
                logger.info("data", data);
                break;
            case 7:

                logger.info("transfer data type = 7");
                payload = transfer.getTransferDataForm().getData();
                logger.info("data", data);
                break;
            case 8:
                logger.info("transfer data type = 8");
                tdf = dataRequestSingle(transfer.getTransferDataForm());

                result.setOutgoingTransfer(transfer);

                parser = new JSONParser();
                obj = (JSONObject) parser.parse(tdf.getData());

                payload = new String(Base64.getDecoder().decode((String) obj.get("userData")));
                logger.info("data", data);
                break;

            default:
                data = "";
                payload = "";
        }

        result.setData(data);
        result.setPayload(payload);

        return result;

    }

}
