package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet4CheckRest extends Packet {

    private int usr_id;


    public Packet4CheckRest(byte[] data) {
        super(4);
        this.usr_id = parser.getUsrId(data);
        this.setPacketString();
    }


    public Packet4CheckRest(int user_id) {
        super(4);
        this.usr_id = user_id;
        this.setPacketString();
    }


    //@TODO
    protected void setPacketString() {
        this.packetString = ("4" + "\n" + this.usr_id + "\n");

    }


    public int getUsrId() {
        return usr_id;
    }


}
