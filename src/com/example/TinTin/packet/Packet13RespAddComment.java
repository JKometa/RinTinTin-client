package com.example.TinTin.packet;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class Packet13RespAddComment extends Packet {

    private int comment_id;


    public Packet13RespAddComment(byte[] data) {
        super(13);
        this.comment_id = parser.getId(new String(data));

    }

    public int getComment_id() {
        return this.comment_id;
    }


    @Override
    protected void setPacketString() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
