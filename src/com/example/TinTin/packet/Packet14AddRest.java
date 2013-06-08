package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet14AddRest extends Packet {

    private String name;
    private String address;
    private String type;


    public Packet14AddRest(String nazwa, String adres, String typ) {
        super(14);

        this.name = nazwa;
        this.address = adres;
        this.type = typ;
        this.setPacketString();
    }


    //@TODO
    protected void setPacketString() {
        this.packetString = "14" + "\n" + this.name + "\n" + this.address + "\n" + this.type + "\n";

    }
}
