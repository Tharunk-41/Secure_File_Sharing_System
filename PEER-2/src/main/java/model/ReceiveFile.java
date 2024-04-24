package model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ReceiveFile {
    private static ReceiveFile robj = new ReceiveFile();

    private ReceiveFile() {
    }

    public static ReceiveFile getInstance() {
        return robj;
    }

    public byte[] decrypt(File file, Crypto c)
            throws IOException, FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return c.decrypt(file);
    }

    public myFile receive(String IP, int port, Crypto c, String name, String type, String checksum) throws Exception {
        boolean serverOpen = false;
        System.out.println(type);
        while (!serverOpen) {
            try (Socket socket = new Socket(InetAddress.getByName(IP), port);
                    FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/tmp1");
                    BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                byte[] contents = new byte[10000];
                InputStream is = socket.getInputStream();
                int bytesRead = 0;
                while ((bytesRead = is.read(contents)) != -1)
                    bos.write(contents, 0, bytesRead);
                bos.flush();
                socket.close();

                File output = new File(System.getProperty("java.io.tmpdir") + "/tmp1");

                byte[] decrypted = decrypt(output, c);

                try (OutputStream os = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/res")) {
                    os.write(decrypted);

                    File file = new File(System.getProperty("java.io.tmpdir") + "/res");
                    byte[] arr = new byte[(int) file.length()];
                    try (InputStream fl = new FileInputStream(file)) {
                        fl.read(arr);
                    }

                    String checkSumVer = c.calculateChecksum(arr);

                    if (!checkSumVer.equals(checksum)) {
                        System.out.println("C1:" + checksum);
                        System.out.println("C2:" + checkSumVer);
                        return null;
                    }

                    // Step 1: Create a Concrete Builder
                    myFile.Builder builder = new myFile.ConcreteBuilder();

                    // Step 2: Create a Director
                    myFile.Director director = new myFile.Director();

                    // Step 3: Construct the myFile instance using the Director
                    myFile mf = director.constructFile(
                            builder,
                            name,
                            type,
                            "" + file.length(),
                            arr);

                    return mf;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }
}