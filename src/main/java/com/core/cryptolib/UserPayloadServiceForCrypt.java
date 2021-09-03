package com.core.cryptolib;

import com.core.cryptolib.components.Settings;
import com.core.cryptolib.enums.FirmwareOrderEnum;
import com.core.cryptolib.enums.FirmwareStatusEnum;
import com.core.cryptolib.enums.InfoRequestType;
import com.core.cryptolib.forms.FirmwareRequestForm;
import com.core.cryptolib.forms.FirmwareResponseForm;
import com.core.cryptolib.forms.ResponseTDPublicIdForm;
import com.core.cryptolib.forms.TransferDataForm;
import com.core.cryptolib.forms.TransferDataFormWithDevicesInfo;
import com.core.cryptolib.forms.TrustedDeviceForm;

import com.core.cryptolib.services.des.enums.Algorithm;
import com.core.cryptolib.services.des.enums.ChipperMode;
import com.core.cryptolib.services.des.enums.DesPaddingMode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.CRC32;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UserPayloadServiceForCrypt {

    JSONObject decodedJSONData;

    Settings settings;

    EncryptService desApp;

    public UserPayloadServiceForCrypt(Settings settings) {
        this.settings = settings;
        this.desApp = new EncryptService();
    }


    /*

    Декодировали, получили trustedDevicePublicId отправителя и получателя,
    вызвали метод с уровня приложения, который:
1. достал из БД два объекта устройства (в котором ключи и идентификаторы)
2. сгенерировал ключи (но пока не сохранил их в БД) и передал в ядро
Далее библиотека проверила запрос, если всё ок - callback с разрешением,
    в этом момент уровень приложения сохраняет новые ключи.

     */
//       byte[] senderDeviceNewKey = EncryptService.getSecureRandom(8);
//
//        byte[] recipientDeviceNewKey = EncryptService.getSecureRandom(8);
    public ResponseTDPublicIdForm twiceEncryptedPermissionBegin(TransferDataForm data) throws ParseException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        if (data.getType() != InfoRequestType.twiceEncryptedRequest.getValue()) {
            return null;
        }

        JSONParser parser = new JSONParser();
        decodedJSONData = (JSONObject) parser.parse(data.getData());

        String tdRecipientTrustedDevicePublicId = new String(Base64.getDecoder()
                .decode(decodedJSONData
                        .get("recipientTrustedDevicePublicId")
                        .toString()
                        .getBytes()));
        String tdSenderTrustedDevicePublicId = new String(Base64.getDecoder()
                .decode(decodedJSONData
                        .get("senderTrustedDevicePublicId")
                        .toString()
                        .getBytes()));

        ResponseTDPublicIdForm result = new ResponseTDPublicIdForm(
                tdRecipientTrustedDevicePublicId,
                tdSenderTrustedDevicePublicId);

        return result;

    }

    private FirmwareResponseForm firmwareHandler(FirmwareRequestForm firmwareRequestForm) throws FileNotFoundException, IOException {
        FirmwareOrderEnum tmpEnum = null;

        long fileSize = 0l;
        long checkSum = 0l;
        long offset = 0l;

        int size = 0;

        File file;

        String filePart = "";

        switch (firmwareRequestForm.getStatus()) {
            case NOT_READY:
                tmpEnum = FirmwareOrderEnum.PREPARE_UPLOAD;
                break;
            case PREPARE_UPLOAD_FIRMARE:
            case READY_UPLOAD_FIRMAWARE:
            case UPLOADING_FIRMWARE:
                tmpEnum = FirmwareOrderEnum.FILE_PART;

                file = new File(settings.get("pathFirmware").getValue());

                fileSize = file.length();
                checkSum = 0l;
                offset = firmwareRequestForm.getOffset();

                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                String strLine;

                br.skip(offset);
                while ((strLine = br.readLine()) != null || size < 1024) {
                    filePart = filePart
                            .concat(strLine)
                            .concat("\r\n");
                    size += strLine.length();
                }

                if (firmwareRequestForm.getOffset() == fileSize) {
                    tmpEnum = FirmwareOrderEnum.CHECKSUM;

                    CRC32 fileCRC32 = new CRC32();
                    fileCRC32.update(fis.readAllBytes());

                    checkSum = fileCRC32.getValue();
                }

                break;
        }

        FirmwareResponseForm firmwareResponseForm = new FirmwareResponseForm(
                tmpEnum,
                size,
                filePart,
                checkSum);

        return firmwareResponseForm;
    }

//      TrustedDevice tdRecipientOpen = tdRepository.findTrustedDeviceByDevicePublicId(tdRecipientTrustedDevicePublicId);
//        TrustedDevice tdSenderOpen = tdRepository.findTrustedDeviceByDevicePublicId(tdSenderTrustedDevicePublicId);
    public TransferDataFormWithDevicesInfo twiceEncryptedPermissionEnd(
            TrustedDeviceForm tdRecipient,
            TrustedDeviceForm tdSender,
            byte[] senderDeviceNewKey,
            byte[] recipientDeviceNewKey,
            byte[] recipientDeviceResetKey,//добавился
            byte[] senderDeviceResetKey,//добавился
            int maxAttemps
    ) throws UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, ParseException, IOException {

        if (tdRecipient == null || tdSender == null) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }

        byte[] recipientDeviceActualKey = null,
                senderDeviceActualKey = null,
                d2 = null,
                IV = null,
                encoded = null,
                bytes = null,
                decryptedData = null;

        SecretKey key = null;

        String decodedTDPrivateId = "";

        JSONParser parser = new JSONParser();

        JSONObject resultDecryptedJSON = null;

        FirmwareRequestForm firmwareRequestForm = null;

        int steps = 0;

        while (true) {

            if (tdRecipient.getAttempts() >= maxAttemps || steps == 2) {
                tdRecipient.setAttempts(tdRecipient.getAttempts() + 1);
                return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
            }

            //ключи в base64
            recipientDeviceActualKey = Base64.getDecoder().decode(
                    steps == 0 ? tdRecipient.getDeviceActualKey() : tdRecipient.getDeviceOldKey()
            );// getDeviceActualKey();

            d2 = Base64.getDecoder().decode(decodedJSONData
                    .get("encryptedDataExchangeRequest")
                    .toString());

            IV = Arrays.copyOfRange(d2, 0, 8);
            encoded = Arrays.copyOfRange(d2, 8, d2.length);

            bytes = recipientDeviceActualKey;
            key = EncryptService.getKeyFromBytes(bytes);

            try {
                decryptedData = desApp.decryptToByte(encoded,
                        key,
                        EncryptService.getAlgo(Algorithm.DES.getValue(),
                                ChipperMode.CBC.getValue(),
                                DesPaddingMode.PKCS7_PADDING.getValue()),
                        IV);

                resultDecryptedJSON = (JSONObject) parser.parse(new String(decryptedData));
                decodedTDPrivateId = (String) resultDecryptedJSON.get("trustedDevicePrivateId");

            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ParseException e) {
                steps++;

                continue;
            }

            break;
        }

        //TrustedDevice tdRecipient = tdRepository.findTrustedDeviceByDevicePrivateId(decodedTDPrivateId);
        if (!tdRecipient.getDevicePrivateId().equals(decodedTDPrivateId)) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }

        if (resultDecryptedJSON.containsKey("firmware")) {
            firmwareRequestForm = new FirmwareRequestForm((JSONObject) resultDecryptedJSON.get("firmware"));
        }

        steps = 0;
        while (true) {

            if (tdSender.getAttempts() >= maxAttemps || steps == 2) {

                tdSender.setAttempts(tdSender.getAttempts() + 1);

                return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
            }

            senderDeviceActualKey = Base64.getDecoder().decode(
                    steps == 0 ? tdSender.getDeviceActualKey()
                            : tdSender.getDeviceOldKey()
            );//.getDeviceActualKey();

            d2 = Base64
                    .getDecoder()
                    .decode((String) resultDecryptedJSON.get("encryptedDataExchangeRequest"));

            IV = Arrays.copyOfRange(d2, 0, 8);
            encoded = Arrays.copyOfRange(d2, 8, d2.length);

            bytes = senderDeviceActualKey;
            key = EncryptService.getKeyFromBytes(bytes);

            try {
                decryptedData = desApp.decryptToByte(encoded,
                        key,
                        EncryptService.getAlgo(Algorithm.DES.getValue(),
                                ChipperMode.CBC.getValue(),
                                DesPaddingMode.PKCS7_PADDING.getValue()),
                        IV);

                resultDecryptedJSON = (JSONObject) parser.parse(new String(decryptedData));
                decodedTDPrivateId = (String) resultDecryptedJSON.get("trustedDevicePrivateId");
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ParseException e) {
                steps++;

                continue;
            }

            break;
        }

        if (!tdSender.getDevicePrivateId().equals(decodedTDPrivateId)) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }

        String recipientTrustedDevicePublicId = Base64.getEncoder()
                .encodeToString(tdRecipient.getDevicePublicId().getBytes()); //DevicePublicId()
        String senderTrustedDeviceNewKey = Base64.getEncoder().encodeToString(senderDeviceNewKey);
        String senderTrustedDeviceResetKey = Base64.getEncoder().encodeToString(senderDeviceResetKey);

        JSONObject encryptedDataExchangePermission = new JSONObject();
        encryptedDataExchangePermission.put("recipientTrustedDevicePublicId", recipientTrustedDevicePublicId);
        encryptedDataExchangePermission.put("senderTrustedDeviceActualKey", senderTrustedDeviceNewKey);
        encryptedDataExchangePermission.put("forwardAlgorithm", 0);
        encryptedDataExchangePermission.put("backwardAlgorithm", 0);
        encryptedDataExchangePermission.put("senderTrustedDeviceResetKey", senderTrustedDeviceResetKey);

        if (firmwareRequestForm != null) {

            tdSender.setCurrentFirmware(firmwareRequestForm.getVersion());

            if (!tdSender.getCurrentFirmware().equals(settings.get("actualFirmware").getValue())) {
                encryptedDataExchangePermission.put("firmawere", firmwareHandler(firmwareRequestForm).toJSON());

            }

        }

        IV = desApp.getIV(8);
        bytes = senderDeviceActualKey; //??
        key = EncryptService.getKeyFromBytes(bytes);

        byte[] encryptedSenderData = desApp.encryptToByte(encryptedDataExchangePermission
                .toJSONString().getBytes(),
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        byte[] resultSender = new byte[IV.length + encryptedSenderData.length];
        System.arraycopy(IV, 0, resultSender, 0, IV.length);
        System.arraycopy(encryptedSenderData, 0, resultSender, IV.length, encryptedSenderData.length);

        String eDEP = new String(Base64.getEncoder().encode(resultSender));
        String rTDAK = new String(Base64.getEncoder().encode(recipientDeviceNewKey));
        String rTDARK = new String(Base64.getEncoder().encode(recipientDeviceResetKey));
        String sTDAK = new String(Base64.getEncoder().encode(senderDeviceNewKey));
        String sTDPId = new String(Base64.getEncoder()
                .encode(tdSender.getDevicePublicId().getBytes())); //DevicePublicId()

        JSONObject objSenderData = new JSONObject();
        objSenderData.put("encryptedDataExchangePermission", eDEP);
        objSenderData.put("backwardAlgorithm", 0);
        objSenderData.put("forwardAlgorithm", 0);
        objSenderData.put("recipientTrustedDeviceActualKey", rTDAK);
        objSenderData.put("senderTrustedDeviceActualKey", sTDAK);
        objSenderData.put("senderTrustedDevicePublicId", sTDPId);
        objSenderData.put("recipientTrustedDeviceResetKey", rTDARK);

        if (firmwareRequestForm != null) {

            tdRecipient.setCurrentFirmware(firmwareRequestForm.getVersion());

            if (!tdRecipient.getCurrentFirmware().equals(settings.get("actualFirmware").getValue())) {
                objSenderData.put("firmware", firmwareHandler(firmwareRequestForm).toJSON());
            }
        }

        SecretKey rKey = EncryptService.getKeyFromBytes(recipientDeviceActualKey);//???
        byte[] encryptedRecipientData = desApp.encryptToByte(objSenderData.toJSONString().getBytes(),
                rKey,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        byte[] resultRecipient = new byte[IV.length + encryptedRecipientData.length];
        System.arraycopy(IV, 0, resultRecipient, 0, IV.length);
        System.arraycopy(encryptedRecipientData, 0, resultRecipient, IV.length, encryptedRecipientData.length);

        TransferDataForm tdf = new TransferDataForm();
        tdf.setType(InfoRequestType.twiceEncryptedPermission.getValue());
        tdf.setData(resultRecipient);

        return new TransferDataFormWithDevicesInfo(tdf, tdSender, tdRecipient);
    }

//    tdSender.setDeviceOldKeyEncode (tdSender.getDeviceActualKey
//
//    
//
//    
//
//    
//
//    ());
//    tdSender.setDeviceActualKeyEncode (senderDeviceNewKey);
//
//    tdRepository.save (tdSender);
//
//    tdRecipient.setDeviceOldKeyEncode (tdRecipient.getDeviceActualKey
//
//
//
//    
//
//    
//
//    
//
//    
//
//    ());
//    tdRecipient.setDeviceActualKeyEncode (recipientDeviceNewKey);
//
//    tdRepository.save (tdRecipient);
    public TransferDataForm denailRequest() throws UnsupportedEncodingException {

        TransferDataForm tdf = new TransferDataForm();

        tdf.setType(InfoRequestType.denial.getValue());
        tdf.setData("");

        return tdf;

    }

    public Settings getSettings() {
        return settings;
    }

    
    
}
