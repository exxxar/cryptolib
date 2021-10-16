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

    CryptoLoggerService logger;

    public UserPayloadServiceForCrypt(Settings settings, CryptoLoggerService logger) {
        this.settings = settings;
        this.desApp = new EncryptService();
        this.logger = logger;
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

    private boolean acceptFirmwareUpdate(){
        if (!settings.isExist("acceptFirmwareUpdate")) {
            return false;
        }

        if (!Boolean.parseBoolean(settings.get("acceptFirmwareUpdate").getValue())) {
            return false;
        }
        
        return true;
    }
    
    private FirmwareResponseForm firmwareHandler(FirmwareRequestForm firmwareRequestForm) {

        if (!this.acceptFirmwareUpdate()) {
            return new FirmwareResponseForm();
        }

      
        
        try {
            logger.info("reencrypt file start");

            FirmwareOrderEnum tmpEnum = null;

            long fileSize = 0l;
            long checkSum = 0l;
            long offset = 0l;

            int size = 0;

            File file = new File(settings.get("pathFirmware").getValue());

            if (!file.exists() || file.isDirectory()) {
                logger.info("reencrypt not exist!!");
                return new FirmwareResponseForm();
            }

            String filePart = "";

            switch ((int) firmwareRequestForm.getStatus()) {
                case 0:
                    tmpEnum = FirmwareOrderEnum.PREPARE_UPLOAD;
                    break;
                case 1:
                case 2:
                case 3:
                    tmpEnum = FirmwareOrderEnum.FILE_PART;

                    fileSize = file.length();
                    checkSum = 0l;
                    offset = firmwareRequestForm.getOffset();

                    FileInputStream fis = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                    if (firmwareRequestForm.getOffset() == fileSize) {
                        tmpEnum = FirmwareOrderEnum.CHECKSUM;

                        file = new File(settings.get("pathFirmware").getValue());
                        fis = new FileInputStream(file);

                        byte[] f = fis.readAllBytes();

                        CRC32 fileCRC32 = new CRC32();
                        fileCRC32.update(f);

                        checkSum = fileCRC32.getValue();

                        fis.close();
                        break;
                    }

                    br.skip(offset);

                    while (true) {

                        int a = br.read();

                        char c = (char) a;
                        filePart = filePart
                                .concat("" + c);

                        size += 1;

                        if ((size >= 5120 && a == 10) || (firmwareRequestForm.getOffset() + size == fileSize)) {
                            break;
                        }

                    }
                    br.close();
                    fis.close();

                    break;
            }

            FirmwareResponseForm firmwareResponseForm = new FirmwareResponseForm(
                    tmpEnum,
                    offset,
                    filePart,
                    checkSum);

            logger.info("reencrypt file success" + firmwareResponseForm.toJSON().toJSONString());

            return firmwareResponseForm;
        } catch (IOException e) {
            logger.info("reencrypt file crashed");
            return new FirmwareResponseForm();
        }
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

        boolean isEncryptByResetSenderKey = false;
        boolean isEncryptByResetRecipientKey = false;
        

        if (tdRecipient == null || tdSender == null) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }
        
        boolean isNextStepAfterReset = tdRecipient.isResetTry() || tdSender.isResetTry();

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

        if (tdRecipient.getDeviceActualKey().equals(tdRecipient.getDeviceResetKey())
                || (isNextStepAfterReset && tdRecipient.isAcceptAutoReset())) {
            tdRecipient.setDeviceResetKey(Base64.getEncoder().encodeToString(recipientDeviceResetKey));
            tdRecipient.setAttempts(0);
            tdRecipient.setResetTry(false);
        }

        while (true) {

            if (tdRecipient.getAttempts() >= maxAttemps && !tdRecipient.isAcceptAutoReset() || steps == 2) {
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

                if (steps == 2 && tdRecipient.isAcceptAutoReset() && !tdRecipient.isResetTry()) {
                    tdRecipient.setDeviceOldKey(tdRecipient.getDeviceResetKey());
                    isEncryptByResetRecipientKey = true;
                    tdRecipient.setResetTry(true);
                    break;
                }

                continue;
            }

            break;
        }

        //TrustedDevice tdRecipient = tdRepository.findTrustedDeviceByDevicePrivateId(decodedTDPrivateId);
        if (!tdRecipient.getDevicePrivateId().equals(decodedTDPrivateId) && !isEncryptByResetRecipientKey) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }

        logger.info("tdRecipient result json=>" + resultDecryptedJSON.toJSONString());
//
//        if (resultDecryptedJSON.containsKey("firmware")) {
//            firmwareRequestForm = new FirmwareRequestForm((JSONObject) resultDecryptedJSON.get("firmware"));
//        }

        steps = 0;

        if (tdSender.getDeviceActualKey().equals(tdSender.getDeviceResetKey())
                || (isNextStepAfterReset && tdSender.isAcceptAutoReset())) {

            logger.info("tdSender try1=>" + tdSender.isResetTry());

            tdSender.setDeviceResetKey(Base64.getEncoder().encodeToString(senderDeviceResetKey));
            tdSender.setAttempts(0);
            tdSender.setResetTry(false);

            logger.info("tdSender try2=>" + tdSender.isResetTry());
        }

        while (true) {

            if (tdSender.getAttempts() >= maxAttemps && !tdSender.isAcceptAutoReset() || steps == 2) {

                tdSender.setAttempts(tdSender.getAttempts() + 1);

                logger.info("tdSender attemps=>" + tdSender.getAttempts());

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

                logger.info("tdSender (crash) step=>" + steps);

                if (steps == 2 && tdSender.isAcceptAutoReset() && !tdSender.isResetTry()) {
                    tdSender.setDeviceOldKey(tdSender.getDeviceResetKey());
                    isEncryptByResetSenderKey = true;
                    tdSender.setResetTry(true);

                    logger.info("tdSender start attemps=>" + tdSender.getAttempts());
                    break;
                }

                continue;
            }

            break;
        }

        logger.info("tdSender after attemps");

        if (!tdSender.getDevicePrivateId().equals(decodedTDPrivateId) && !isEncryptByResetSenderKey) {
            return new TransferDataFormWithDevicesInfo(denailRequest(), tdSender, tdRecipient);
        }

        logger.info("tdSender result json=>" + resultDecryptedJSON.toJSONString());

        if (resultDecryptedJSON.containsKey("firmware")&&this.acceptFirmwareUpdate()) {
            firmwareRequestForm = new FirmwareRequestForm((JSONObject) resultDecryptedJSON.get("firmware"));

        }

        String recipientTrustedDevicePublicId = Base64.getEncoder()
                .encodeToString(tdRecipient.getDevicePublicId().getBytes()); //DevicePublicId()
        String senderTrustedDeviceNewKey = Base64.getEncoder().encodeToString(senderDeviceNewKey);
        String senderTrustedDeviceResetKey = tdSender.getDeviceResetKey();

        JSONObject encryptedDataExchangePermission = new JSONObject();
        encryptedDataExchangePermission.put("recipientTrustedDevicePublicId", recipientTrustedDevicePublicId);
        encryptedDataExchangePermission.put("senderTrustedDeviceActualKey", senderTrustedDeviceNewKey);
        encryptedDataExchangePermission.put("forwardAlgorithm", 0);
        encryptedDataExchangePermission.put("backwardAlgorithm", 0);
        encryptedDataExchangePermission.put("senderTrustedDeviceResetKey", senderTrustedDeviceResetKey);

        if (firmwareRequestForm != null&&this.acceptFirmwareUpdate()) {

            tdSender.setCurrentFirmware(firmwareRequestForm.getVersion());

            if (!tdSender.getCurrentFirmware().equals(settings.get("actualFirmware").getValue())) {
                encryptedDataExchangePermission.put("firmware", firmwareHandler(firmwareRequestForm).toJSON());

            }

        }

        logger.info("tdSender encryptedDataExchangePermission json=>" + encryptedDataExchangePermission.toJSONString());

        IV = desApp.getIV(8);

        bytes = isEncryptByResetSenderKey == false
                ? senderDeviceActualKey
                : Base64
                        .getDecoder()
                        .decode(tdSender.getDeviceResetKey());

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
        String rTDARK = tdRecipient.getDeviceResetKey();
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

        if (firmwareRequestForm != null&&this.acceptFirmwareUpdate()) {

            tdRecipient.setCurrentFirmware(firmwareRequestForm.getVersion());

            if (!tdRecipient.getCurrentFirmware().equals(settings.get("actualFirmware").getValue())) {
                objSenderData.put("firmware", firmwareHandler(firmwareRequestForm).toJSON());
            }
        }

        logger.info("tdRecipient objSenderData json=>" + objSenderData.toJSONString());

        SecretKey rKey = isEncryptByResetRecipientKey == false
                ? EncryptService.getKeyFromBytes(recipientDeviceActualKey)
                : EncryptService.getKeyFromBytes(
                        Base64
                                .getDecoder()
                                .decode(tdRecipient.getDeviceResetKey()
                                )
                );

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
