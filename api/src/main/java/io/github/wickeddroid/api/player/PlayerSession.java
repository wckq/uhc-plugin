package io.github.wickeddroid.api.player;

public class PlayerSession {

    private String IP;
    private Long lastConnect;

    public PlayerSession(String ip, Long lastConnect) {
        this.IP = ip;
        this.lastConnect = lastConnect;
    }

    public Long lastConnect() {
        return lastConnect;
    }

    public String IP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void updateLastConnect() {
        this.lastConnect = System.currentTimeMillis();
    }
}
