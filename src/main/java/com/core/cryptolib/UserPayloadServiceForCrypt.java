package com.core.cryptolib;

import com.core.cryptolib.enums.InfoRequestType;
import com.core.cryptolib.forms.TransferDataForm;
import com.core.cryptolib.forms.TrustedDeviceForm;

import com.core.cryptolib.services.des.enums.Algorithm;
import com.core.cryptolib.services.des.enums.ChipperMode;
import com.core.cryptolib.services.des.enums.DesPaddingMode;
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
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UserPayloadServiceForCrypt {

    JSONObject decodedJSONData;

    EncryptService desApp;

    public UserPayloadServiceForCrypt() {
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
    public JSONObject twiceEncryptedPermissionBegin(TransferDataForm data) throws ParseException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        if (data.getType() != InfoRequestType.twiceEncryptedRequest.getValue()) {
            return denailRequest().toJSON();
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

        JSONObject result = new JSONObject();
        result.put("recipient_trusted_device_public_id", tdRecipientTrustedDevicePublicId);
        result.put("sender_trusted_device_public_id", tdSenderTrustedDevicePublicId);
        return result;

    }

//      TrustedDevice tdRecipientOpen = tdRepository.findTrustedDeviceByDevicePublicId(tdRecipientTrustedDevicePublicId);
//        TrustedDevice tdSenderOpen = tdRepository.findTrustedDeviceByDevicePublicId(tdSenderTrustedDevicePublicId);
    public TransferDataForm twiceEncryptedPermissionEnd(
            TrustedDeviceForm tdRecipient,
            TrustedDeviceForm tdSender,
            byte[] senderDeviceNewKey,
            byte[] recipientDeviceNewKey
    ) throws UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, ParseException {

        if (tdRecipient == null || tdSender == null) {
            return denailRequest();
        }

        //ключи в base64
        byte[] recipientDeviceActualKey = Base64.getDecoder().decode(
                tdRecipient.getDeviceActualKey());// getDeviceActualKey();
        byte[] senderDeviceActualKey = Base64.getDecoder().decode(
                tdSender.getDeviceActualKey());//.getDeviceActualKey();

        byte[] d2 = Base64.getDecoder().decode(decodedJSONData
                .get("encryptedDataExchangeRequest")
                .toString());

        byte[] IV = Arrays.copyOfRange(d2, 0, 8);
        byte[] encoded = Arrays.copyOfRange(d2, 8, d2.length);

        byte[] bytes = recipientDeviceActualKey;
        SecretKey key = EncryptService.getKeyFromBytes(bytes);

        byte[] decryptedData = desApp.decryptToByte(encoded,
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        JSONParser parser = new JSONParser();
        JSONObject resultDecryptedJSON = (JSONObject) parser.parse(new String(decryptedData));
        String decodedTDPrivateId = (String) resultDecryptedJSON.get("trustedDevicePrivateId");

        //TrustedDevice tdRecipient = tdRepository.findTrustedDeviceByDevicePrivateId(decodedTDPrivateId);
        if (!tdRecipient.getDevicePrivateId().equals(decodedTDPrivateId)) {
            return denailRequest();
        }

        d2 = Base64
                .getDecoder()
                .decode((String) resultDecryptedJSON.get("encryptedDataExchangeRequest"));

        IV = Arrays.copyOfRange(d2, 0, 8);
        encoded = Arrays.copyOfRange(d2, 8, d2.length);

        bytes = senderDeviceActualKey;
        key = EncryptService.getKeyFromBytes(bytes);

        decryptedData = desApp.decryptToByte(encoded,
                key,
                EncryptService.getAlgo(Algorithm.DES.getValue(),
                        ChipperMode.CBC.getValue(),
                        DesPaddingMode.PKCS7_PADDING.getValue()),
                IV);

        resultDecryptedJSON = (JSONObject) parser.parse(new String(decryptedData));
        decodedTDPrivateId = (String) resultDecryptedJSON.get("trustedDevicePrivateId");

        if (!tdSender.getDevicePrivateId().equals(decodedTDPrivateId)) {
            return denailRequest();
        }

        String recipientTrustedDevicePublicId = Base64.getEncoder()
                .encodeToString(tdRecipient.getDevicePublicId().getBytes()); //DevicePublicId()
        String senderTrustedDeviceNewKey = Base64.getEncoder().encodeToString(senderDeviceNewKey);

        JSONObject encryptedDataExchangePermission = new JSONObject();
        encryptedDataExchangePermission.put("recipientTrustedDevicePublicId", recipientTrustedDevicePublicId);
        encryptedDataExchangePermission.put("senderTrustedDeviceActualKey", senderTrustedDeviceNewKey);
        encryptedDataExchangePermission.put("forwardAlgorithm", 0);
        encryptedDataExchangePermission.put("backwardAlgorithm", 0);

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

        return tdf;
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

}
