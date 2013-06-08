package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet11RespGetComments extends Packet {

    private String login;
    private String description;
    private String date;


    public Packet11RespGetComments(byte[] data) {
        super(11);
        String[] parsed = parser.handleGetComments(new String(data));

        this.login = parsed[0];
        this.description = parsed[1];
        this.date = parsed[2];

    }

    //@TODO
    protected void setPacketString() {


    }

    public String getLogin() {
        return this.login;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }


}
