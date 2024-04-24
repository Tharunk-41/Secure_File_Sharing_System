package model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class Crypto {
    protected final String key;

    public Crypto(String key) {
        this.key = key;
    }

    // Encryption Class
    private class Encryption {
        public byte[] encrypt(File inputFile)
                throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] inputBytes = new byte[(int) inputFile.length()];
            try (FileInputStream inputStream = new FileInputStream(inputFile)) {
                inputStream.read(inputBytes);
            }

            return cipher.doFinal(inputBytes);
        }
    }

    // Decryption Class
    private class Decryption {
        public byte[] decrypt(File inputFile)
                throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] inputBytes = new byte[(int) inputFile.length()];
            try (FileInputStream inputStream = new FileInputStream(inputFile)) {
                inputStream.read(inputBytes);
            }

            return cipher.doFinal(inputBytes);
        }
    }

    // Checksum Calculation Class
    private class ChecksumCalculator {
        public String calculateChecksum(byte[] input) throws NoSuchAlgorithmException, IOException {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            InputStream fileInput = new ByteArrayInputStream(input);
            byte[] dataBytes = new byte[1024];

            int bytesRead;
            while ((bytesRead = fileInput.read(dataBytes)) != -1) {
                messageDigest.update(dataBytes, 0, bytesRead);
            }
            byte[] digestBytes = messageDigest.digest();

            StringBuilder sb = new StringBuilder();

            for (byte digestByte : digestBytes) {
                sb.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
            }

            String checksumValue = sb.toString();

            fileInput.close();
            return checksumValue;
        }
    }

    // Public methods for accessing nested classes

    public byte[] encrypt(File inputFile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new Encryption().encrypt(inputFile);
    }

    public byte[] decrypt(File inputFile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new Decryption().decrypt(inputFile);
    }

    public String calculateChecksum(byte[] input) throws NoSuchAlgorithmException, IOException {
        return new ChecksumCalculator().calculateChecksum(input);
    }
}
