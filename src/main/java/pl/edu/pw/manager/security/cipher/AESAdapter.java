package pl.edu.pw.manager.security.cipher;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AESAdapter {

    private final String algorithm = "AES/CBC/PKCS5Padding";
    private final int hashIteration = 65536;
    private final int keyLength = 256;
    private final int blockSize = 16;
    private final String salt = "=T:TqUTjK(LR(#q*";


    public String encrypt(String input, String password) throws Exception {
        SecretKey key = getKeyFromPassword(password);
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(concatenate(iv.getIV(), cipherText));
    }

    public String decrypt(String cipherText, String password) throws Exception {
        SecretKey key = getKeyFromPassword(password);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, generateIv());

        byte[] byteText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        byte[] plainText = Arrays.copyOfRange(byteText, blockSize, byteText.length);

        return new String(plainText);
    }

    private SecretKey getKeyFromPassword(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), hashIteration, keyLength);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private byte[] concatenate(byte[] x, byte[] y) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(x);
        outputStream.write(y);

        return outputStream.toByteArray();
    }

}
