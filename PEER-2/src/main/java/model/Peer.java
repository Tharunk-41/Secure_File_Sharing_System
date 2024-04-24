package model;

public class Peer extends Client {
    String id;

    public Peer(String id_) {
        super(null, null);
        id = id_;
        int min = 5000;
        int max = 8000;
        int port_ = (int) (Math.random() * (max - min + 1) + min);
        port = Integer.toString(port_);
        IP = "127.0.0.1";
    }

    @Override
    public String getNickname() {
        return id;
    }
}
