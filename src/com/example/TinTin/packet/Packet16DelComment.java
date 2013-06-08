package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet16DelComment extends Packet {

    private int comment_id;

    public Packet16DelComment(int comment_id) {
        super(16);
        this.comment_id = comment_id;
        this.setPacketString();
    }


    //@TODO
    protected void setPacketString() {
        this.packetString = "16" + "\n" + this.comment_id + "\n";

    }


}
