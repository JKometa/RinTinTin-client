package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet15RespAddRest extends Packet {

    private int usr_id;


    public Packet15RespAddRest(byte[] data) {
        super(0);
        this.usr_id = parser.getUsrId(data);
    }



    public Packet15RespAddRest(int user_id) {
        super(0);
        this.usr_id = user_id;
    }





    public byte[] getData() {
        return ("0" + "\n" + this.usr_id + "\n").getBytes();
    }

    public int getUsrId() {
        return usr_id;
    }



}
