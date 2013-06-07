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
        SNEDNEXT(8), GETCOMM(10),EOD(9), RESPGETCOMM(11), ADDCOMM(12), RESPADDCOMM(13), ADDREST(14), RESPADDREST(15), DELCOMM(16), RESPDELCOMM(17);
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

    public byte packetId;
    public Parser parser = new Parser();
    /**
     * konstruktor pakietu
     */
    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    /**
     * zwraca dane pakietu
     */
    public abstract byte[] getData();
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
}
