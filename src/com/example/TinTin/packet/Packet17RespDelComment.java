package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet17RespDelComment extends Packet {

    private int state;


    public Packet17RespDelComment(byte[] data) {
        super(17);
        this.state = Integer.parseInt(parser.getData(new String(data)));

    }

    //@TODO
    protected void setPacketString() {


    }


    public int getState() {
        return this.state;
    }


}
