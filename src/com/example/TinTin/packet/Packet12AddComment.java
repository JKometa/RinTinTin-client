package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet12AddComment extends Packet {

    private int usr_id;
    private int res_id;
    private String date;
    private String description;

    public Packet12AddComment(int usr_id, int res_id, String date, String desc) {
        super(12);
        this.usr_id = usr_id;
        this.res_id = res_id;
        this.date = date;
        this.description = desc;
        this.setPacketString();
    }

    //@TODO
    public void setPacketString() {
        this.packetString = "12" + "\n" + this.usr_id + "\n" + this.res_id + "\n" + date + "\n" + this.description + "\n";
    }

}
