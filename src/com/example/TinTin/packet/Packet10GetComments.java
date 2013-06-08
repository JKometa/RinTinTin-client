package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet10GetComments extends Packet {

    private int res_id;
    private long poczatek;


    public Packet10GetComments(int resID, long date) {
        super(10);

        this.res_id = resID;
        this.poczatek = date;



        this.setPacketString();
    }

    @Override
    public void setPacketString() {

        this.packetString = "10" + "\n" + this.res_id + "\n" + poczatek + "\n";
    }
}
