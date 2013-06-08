package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet6GetRest extends Packet {

    private int date;


    public Packet6GetRest(int date) {
        super(6);
        this.date = date;
        this.setPacketString();
    }


    //@TODO
    protected void setPacketString() {
        this.packetString = ("6" + "\n" + 0 + "\n");

    }


    public int getDate() {
        return date;
    }


}
