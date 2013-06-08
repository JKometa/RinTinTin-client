package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet7RespGetRest extends Packet {

    private int id;
    private String adres;
    private String nazwa;
    private String typ;


    public Packet7RespGetRest(byte[] data) {
        super(7);
        String message = data.toString();
        String[] restaurant = parser.handleGetRestaurants(message);
        this.id = Integer.parseInt(restaurant[0]);
        this.adres = restaurant[1];
        this.nazwa = restaurant[2];
        this.typ = restaurant[3];
        this.setPacketString();
    }


    public Packet7RespGetRest(int id, String adres, String nazwa, String typ) {
        super(7);
        this.id = id;
        this.adres = adres;
        this.nazwa = nazwa;
        this.typ = typ;
        this.setPacketString();
    }

    //@TODO
    protected void setPacketString() {
        this.packetString = ("7" + "\n" + this.id + "\n" + this.adres + "\n" + this.nazwa + "\n" + this.typ + "\n");

    }

    public int getId() {
        return id;
    }

    public String getAdres() {
        return adres;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getTyp() {
        return typ;
    }


}
