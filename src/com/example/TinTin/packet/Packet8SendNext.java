package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet8SendNext extends Packet {

    public Packet8SendNext() {
        super(8);
        this.setPacketString();
    }

    //@TODO
    protected void setPacketString() {
        this.packetString = ("8" + "\n");

    }


}
