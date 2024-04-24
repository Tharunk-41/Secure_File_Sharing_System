package model;

import java.io.File;

public class Client {
    String IP;
    String port;
    private String nickname;
    private Room room;

    public Client(String nickname_, String link) {
        int min = 5000;
        int max = 9000;
        int port_ = (int) (Math.random() * (max - min + 1) + min);
        port = Integer.toString(port_);
        IP = "127.0.0.1";
        nickname = nickname_;
        room = createRoom(nickname_, link);
    }

    private Room createRoom(String nickname, String link_) {
        Room room_;
        if (nickname == null)
            room_ = null;
        else
            room_ = new Room(nickname, link_);

        return room_;
    }

    public String getIP() {
        return IP;
    }

    public String getPort() {
        return port;
    }

    public Room getRoom() {
        return room;
    }

    public String shareRoom() {
        return room.link;
    }

    public String getNickname() {
        return nickname;
    }

    public void uploadFile(File file) {
        SendFile sendFile = SendFile.getInstance();
        try {
            sendFile.send(file, IP, Integer.parseInt(port), room.cryp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(File file) {
        ReceiveFile receiveFile = ReceiveFile.getInstance();
        try {
            String fileName = file.getName();
            String fileType = getFileType(fileName);
            String checksum = room.cryp.calculateChecksum(file.getName().getBytes());

            receiveFile.receive(IP, Integer.parseInt(port), room.cryp, fileName, fileType, checksum);
            // Process the received file as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileType(String fileName) {
        // Implement this method to determine the file type based on the file name or extension
        // For example, you can use the file extension to determine the file type
        fileName.substring(fileName.lastIndexOf(".") + 1);
        // Map the extension to the appropriate MIME type or file type
        // ...
        return "application/octet-stream"; // Replace with the actual file type
    }
}