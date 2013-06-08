package com.example.TinTin.packet;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet10GetComments extends Packet {

    private int res_id;
    private Date poczatek;


    public Packet10GetComments(int resID, Date date) {
        super(10);

        this.res_id = resID;
        this.poczatek = date;

        if (this.poczatek == null)
            this.poczatek = new Date();

        this.setPacketString();
    }

    @Override
    protected void setPacketString() {

        this.packetString = "10" + "\n" + this.res_id + "\n" + this.poczatek.toString() + "\n";
    }
}
