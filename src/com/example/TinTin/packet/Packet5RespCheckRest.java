package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet5RespCheckRest extends Packet {

    private int usr_id;


    public Packet5RespCheckRest(byte[] data) {
        super(5);
        this.usr_id = parser.getUsrId(data);
    }



    public Packet5RespCheckRest(int user_id) {
        super(5);
        this.usr_id = user_id;
    }





    public byte[] getData() {
        return ("5" + "\n" + this.usr_id + "\n").getBytes();
    }

    public int getUsrId() {
        return usr_id;
    }



}
