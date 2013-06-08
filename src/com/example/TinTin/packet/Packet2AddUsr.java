package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet2AddUsr extends Packet {

    private String login;
    private String passwd;


    public Packet2AddUsr(String login, String passwd) {
        super(2);
        this.login = login;
        this.passwd = passwd;
        this.setPacketString();
    }


    //@TODO
    protected void setPacketString() {
        this.packetString = ("2" + "\n" + this.login + "\n" + this.passwd + "\n");

    }


    public String getLogin() {
        return login;
    }

    public String getPasswd() {
        return passwd;
    }


}
