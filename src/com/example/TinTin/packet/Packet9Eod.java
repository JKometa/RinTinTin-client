package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet9Eod extends Packet {

    public Packet9Eod() {
        super(9);
        this.setPacketString();
    }

    //@TODO
    protected void setPacketString() {
        this.packetString = ("9" + "\n");

    }


}
