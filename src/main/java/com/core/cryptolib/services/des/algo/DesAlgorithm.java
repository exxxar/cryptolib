/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.services.des.algo;

import com.core.cryptolib.services.des.constants.DesConstants;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author SAMS
 */
public class DesAlgorithm implements iDesAlgorithm {

    @Override
    public String encrypt(String data, SecretKey key, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data.getBytes(), DesConstants.iv));
    }

    @Override
    public String encrypt(byte[] data, SecretKey key, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data, DesConstants.iv));
    }

    @Override
    public String decrypt(String data, SecretKey key, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(data), DesConstants.iv));
    }

    @Override
    public String decrypt(byte[] data, SecretKey key, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, data, DesConstants.iv));
    }

    @Override
    public String encrypt(byte[] data, byte[] sKey, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data, DesConstants.iv));
    }

    @Override
    public String encrypt(String data, byte[] sKey, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data.getBytes(), DesConstants.iv));
    }

    @Override
    public String decrypt(String data, byte[] sKey, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(data), DesConstants.iv));
    }

    @Override
    public String decrypt(byte[] data, byte[] sKey, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, data, DesConstants.iv));
    }

    @Override
    public String encryptIVFirst(String data, byte[] sKey, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        SecretKey key = new SecretKeySpec(sKey, algo);
        byte[] IV = null;
        byte[] encoded = null;
        try {
            byte[] d = data.getBytes("UTF-8");
            IV = Arrays.copyOfRange(d, 0, 8);
            encoded = Arrays.copyOfRange(d, 8, d.length);
        } catch (UnsupportedEncodingException ex) {
            IV = DesConstants.iv;
            Logger.getLogger(DesAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, encoded, IV));

    }

    @Override
    public String decryptIVFirst(String data, byte[] sKey, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        SecretKey key = new SecretKeySpec(sKey, algo);

        byte[] d = DatatypeConverter.parseHexBinary(data);
        byte[] IV = Arrays.copyOfRange(d, 0, 8);
        byte[] encoded = Arrays.copyOfRange(d, 8, d.length);

        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, encoded, IV));
    }

    @Override
    public String encryptIVFirst(byte[] data, byte[] sKey, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        SecretKey key = new SecretKeySpec(sKey, algo);
        byte[] IV = Arrays.copyOfRange(data, 0, 8);
        byte[] encoded = Arrays.copyOfRange(data, 8, data.length);
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, encoded, IV));
    }

    @Override
    public String decryptIVFirst(byte[] data, byte[] sKey, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        byte[] IV = Arrays.copyOfRange(data, 0, 8);
        byte[] encoded = Arrays.copyOfRange(data, 8, data.length);
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, encoded, IV));
    }

    @Override
    public String encryptIVFirst(byte[] data, SecretKey sKey, String algo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] IV = Arrays.copyOfRange(data, 0, 8);
        byte[] encoded = Arrays.copyOfRange(data, 8, data.length);
        return byteToString(cryptoData(algo, sKey, Cipher.ENCRYPT_MODE, encoded, IV));
    }

    @Override
    public String decryptIVFirst(byte[] data, SecretKey sKey, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] IV = Arrays.copyOfRange(data, 0, 8);
        byte[] encoded = Arrays.copyOfRange(data, 8, data.length);
        return byteToString(cryptoData(algo, sKey, Cipher.DECRYPT_MODE, encoded, IV));
    }

    @Override
    public String encryptIVFirst(String data, SecretKey key, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] IV = null;
        byte[] encoded = null;
        try {
            byte[] d = data.getBytes("UTF-8");
            IV = Arrays.copyOfRange(d, 0, 8);
            encoded = Arrays.copyOfRange(d, 8, d.length);
        } catch (UnsupportedEncodingException ex) {
            IV = DesConstants.iv;
            Logger.getLogger(DesAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, encoded, IV));
    }

    @Override
    public String decryptIVFirst(String data, SecretKey key, String algo) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        byte[] d = DatatypeConverter.parseHexBinary(data);
        byte[] IV = Arrays.copyOfRange(d, 0, 8);
        byte[] encoded = Arrays.copyOfRange(d, 8, d.length);

        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, encoded, IV));
    }

    @Override
    public String encrypt(byte[] data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data, IV));
    }

    @Override
    public String encrypt(String data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.ENCRYPT_MODE, data.getBytes(), IV));
    }

    @Override
    public String encrypt(byte[] data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, sKey, Cipher.ENCRYPT_MODE, data, IV));
    }

    @Override
    public String encrypt(String data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, sKey, Cipher.ENCRYPT_MODE, data.getBytes(), IV));
    }

    @Override
    public String decrypt(byte[] data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, data, IV));
    }

    @Override
    public String decrypt(String data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return byteToString(cryptoData(algo, key, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(data), IV));
    }

    @Override
    public String decrypt(byte[] data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, sKey, Cipher.DECRYPT_MODE, data, IV));
    }

    @Override
    public String decrypt(String data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return byteToString(cryptoData(algo, sKey, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(data), IV));

    }

    @Override
    public  byte[] getIV(String algo) throws NoSuchAlgorithmException, NoSuchPaddingException{
          Cipher cipher = Cipher.getInstance(algo);
          return cipher.getIV();
    }
    
    private byte[] cryptoData(String algo, SecretKey key, int encryptMode, byte[] bytes, byte[] IV) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        
        Cipher cipher = Cipher.getInstance(algo);
       
        if (algo.contains("ECB")) {
            cipher.init(encryptMode, key);
        } else {
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV);
            cipher.init(encryptMode, key, paramSpec);
        }
        return cipher.doFinal(bytes);

    }

    private String byteToString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    @Override
    public byte[] encryptToByte(String data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return cryptoData(algo, key, Cipher.ENCRYPT_MODE, data.getBytes(), IV);
    }

    @Override
    public byte[] decryptToByte(String data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return cryptoData(algo, key, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(data), IV);
    }

    @Override
    public byte[] encryptToByte(String data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return cryptoData(algo, sKey, Cipher.ENCRYPT_MODE, data.getBytes(), IV);
    }

    @Override
    public byte[] encryptToByte(byte[] data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return cryptoData(algo, sKey, Cipher.ENCRYPT_MODE, data, IV);
    }

    @Override
    public byte[] encryptToByte(byte[] data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return cryptoData(algo, key, Cipher.ENCRYPT_MODE, data, IV);
    }

    @Override
    public byte[] decryptToByte(byte[] data, byte[] sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(sKey, algo);
        return cryptoData(algo, key, Cipher.DECRYPT_MODE, data, IV);
    }

    @Override
    public byte[] decryptToByte(byte[] data, SecretKey sKey, String algo, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return cryptoData(algo, sKey, Cipher.DECRYPT_MODE, data, IV);
    }

    @Override
    public byte[] decryptToByte(String dataToDecrypt, SecretKey sKey, String algo, byte[] IV)  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
      return cryptoData(algo, sKey, Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(dataToDecrypt),IV);
    }

}
