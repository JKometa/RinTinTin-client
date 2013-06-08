package com.example.TinTin.packet;

import com.example.TinTin.Parser;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 07.06.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */

public abstract class Packet {
    /**
     * mozliwe typy pakietow
     */
    public static enum PacketTypes {
        INVALID(-1), PING(0), PONG(1), ADDUSR(2), RESPADDUS(3), CHECKREST(4), RESPCHECKREST(5), GETREST(6), RESPGETREST(7),
        SNEDNEXT(8), GETCOMM(10), EOD(9), RESPGETCOMM(11), ADDCOMM(12), RESPADDCOMM(13), ADDREST(14), RESPADDREST(15), DELCOMM(16), RESPDELCOMM(17);
        /**
         * id pakietu
         */
        private int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    protected String packetString;

    protected abstract void setPacketString();

    public byte packetId;
    public Parser parser = new Parser();

    /**
     * konstruktor pakietu
     */
    public Packet(int packetId) {
        this.packetId = (byte) packetId;
        System.out.println("Siema");
    }

    /**
     * zwraca dane pakietu
     */
    public byte[] getData() throws EmptyPacketStringException {

        if (this.packetString == null) {
            throw new EmptyPacketStringException();
        }

        return this.packetString.getBytes();
    }

    /**
     * zwraca typ pakietu
     */
    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }

    /**
     * sprawdza typ pakietu
     */
    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes p : PacketTypes.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

    /**
     * Tworzy tablice bajtow pakietu poprzedzona wielkoscia SAMEGO pakietu
     * <p/>
     * Skladnia zwracanego pakietu(po stworzeniu stringa) size\npakiet
     *
     * @return pakiet z wilkoscia
     */
    protected byte[] getPacket() {

        byte[] data = new byte[0];
        try {
            data = this.getData();
        } catch (EmptyPacketStringException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        int size = data.length;
        byte[] sByte = String.valueOf(size).getBytes();

        byte[] ret = new byte[size + sByte.length + 1];
        int i = 0;
        for (; i < sByte.length; ++i) {
            ret[i] = sByte[i];
        }
        ret[i++] = (byte) '\n';

        for (int j = 0; j < size; ++j) {
            ret[i + j] = data[j];
        }
        return ret;

    }


}
