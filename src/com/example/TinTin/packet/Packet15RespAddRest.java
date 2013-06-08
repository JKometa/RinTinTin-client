package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet15RespAddRest extends Packet {

    private int state;

    public Packet15RespAddRest(byte[] data) {
        super(15);
        this.state = Integer.parseInt(parser.getData(new String(data)));
    }


    //@TODO
    protected void setPacketString() {

    }

    /**
     * @return Zwraca status czy się udało dodać czy nie
     */
    public int getState() {
        return this.state;
    }

}
