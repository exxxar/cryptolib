/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib;

import com.core.cryptolib.services.des.algo.DesAlgorithm;
import com.core.cryptolib.services.des.algo.iDesAlgorithm;
import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import javax.crypto.spec.DESKeySpec;

public class EncryptService {

    iDesAlgorithm encryptAlgorithm;

    public EncryptService() {
        this.encryptAlgorithm = new DesAlgorithm();
    }

    public byte[] encryptToByte(String dataToEncrypt, SecretKey key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.encryptToByte(dataToEncrypt, key, algo, IV);
    }

    public byte[] decryptToByte(String dataToDecrypt, SecretKey key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptToByte(dataToDecrypt, key, algo, IV);
    }

    public byte[] encryptToByte(byte[] dataToEncrypt, byte[] key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.encryptToByte(dataToEncrypt, key, algo, IV);
    }

    public byte[] decryptToByte(byte[] dataToDecrypt, byte[] key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptToByte(dataToDecrypt, key, algo, IV);
    }

    public byte[] encryptToByte(String dataToEncrypt, byte[] key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.encryptToByte(dataToEncrypt, key, algo, IV);
    }

    public byte[] decryptToByte(String dataToDecrypt, byte[] key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptToByte(dataToDecrypt, key, algo, IV);
    }

    public String encrypt(String dataToEncrypt, SecretKey key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo);
    }

    public String encrypt(byte[] dataToEncrypt, SecretKey key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo);
    }

    public String decrypt(String dataToDecrypt, SecretKey key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo);
    }

    public String decrypt(byte[] dataToDecrypt, SecretKey key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo);
    }

    public String encrypt(String dataToEncrypt, byte[] key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo);
    }

    public String decrypt(String dataToDecrypt, byte[] key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo);
    }

    public String encrypt(String dataToEncrypt, SecretKey key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo, IV);
    }

    public String encrypt(byte[] dataToEncrypt, SecretKey key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo, IV);
    }

    public String decrypt(String dataToDecrypt, SecretKey key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo, IV);
    }

    public String decrypt(byte[] dataToDecrypt, SecretKey key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo, IV);
    }

    public String encrypt(String dataToEncrypt, byte[] key, String algo, byte[] IV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encrypt(dataToEncrypt, key, algo, IV);
    }

    public String decrypt(String dataToDecrypt, byte[] key, String algo, byte[] IV) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decrypt(dataToDecrypt, key, algo, IV);
    }

    public String encryptIVFirst(String dataToEncrypt, SecretKey key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encryptIVFirst(dataToEncrypt, key, algo);
    }

    public String encryptIVFirst(byte[] dataToEncrypt, SecretKey key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encryptIVFirst(dataToEncrypt, key, algo);
    }

    public String decryptIVFirst(String dataToDecrypt, SecretKey key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptIVFirst(dataToDecrypt, key, algo);
    }

    public String decryptIVFirst(byte[] dataToDecrypt, SecretKey key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptIVFirst(dataToDecrypt, key, algo);
    }

    public String encryptIVFirst(String dataToEncrypt, byte[] key, String algo) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        return encryptAlgorithm.encryptIVFirst(dataToEncrypt, key, algo);
    }

    public String decryptIVFirst(String dataToDecrypt, byte[] key, String algo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return encryptAlgorithm.decryptIVFirst(dataToDecrypt, key, algo);
    }

    public static String getAlgo(String algo, String cipherMode, String paddingMode) {
        return algo + "/" + cipherMode + "/" + paddingMode;
    }

    public byte[] getIV(int size) throws NoSuchAlgorithmException {
        return EncryptService.getSecureRandom(size);
    }

    public static SecretKey getSecretKey(String algo) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algo);
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 56;
        keyGenerator.init(keyBitSize, secureRandom);
        return keyGenerator.generateKey();
    }

    public static byte[] getSecureRandom(int keyBitSize) throws NoSuchAlgorithmException {

        Random rd = new Random();
        byte[] arr = new byte[keyBitSize];
        rd.nextBytes(arr);
        return arr;
    }

    public static SecretKey getKeyFromString(String password) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {

        byte[] bytes = password.getBytes();
        DESKeySpec keySpec = new DESKeySpec(bytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
    }

    public static SecretKey getKeyFromBytes(byte[] bytes) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {

        DESKeySpec keySpec = new DESKeySpec(bytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
    }

    public byte[] decryptToByte(byte[] dataToDecrypt, SecretKey key, String algo, byte[] IV) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return encryptAlgorithm.decryptToByte(dataToDecrypt, key, algo, IV);
    }

    public byte[] encryptToByte(byte[] decryptedData, SecretKey key, String algo, byte[] IV) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        return encryptAlgorithm.encryptToByte(decryptedData, key, algo, IV);
    }
}
