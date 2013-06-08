package com.example.TinTin;

import android.os.AsyncTask;
import com.example.TinTin.packet.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 31.05.13
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    static public  Object signal = new Object();
    static public  Object destroySignal = new Object();
    private static boolean doTheMagic = true;
    private static byte[] data;



    public byte[] recivedData(){
        return data;
    }
    public boolean handleDeleteComment(String message) {
        int response =  getId(message);
        if(response == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public int handleAddRestaurant(String message) {
        int restId =  getId(message);
        return restId;

    }

    public int hanldeAddComment(String message) {
        int commentId =  getId(message);
        return commentId;
    }

    public String[] handleGetComments(String message) {
        if(message != null){
            String allData[] = new String[3];
            String data = message;
            String login = getStringFromPacket(data);
            data = getData(data);
            String text =getStringFromPacket(data);
            data = getData(data);
            String date = getStringFromPacket(data);
            allData[0] = login + "  ";
            allData[1] = text + "  ";
            allData[2] = date + "  ";
            return allData;




        }
        return null;
    }

    public String[] handleGetRestaurants(String message) {
        if(message != null){

            String[] allData = new String[4];
            String data = message;
            String resId =  getStringFromPacket(data);

            data = getData(data);

            String resName = getStringFromPacket(data);
            data = getData(data);
            String resAddress =getStringFromPacket(data);
            data = getData(data);

            String resType = getStringFromPacket(data);

            allData[0] = resId;
            allData[1] = resAddress + "  ";
            allData[2] = resName + "  ";
            allData[3] = resType + "  ";
            return allData;

        }
        return null;
    }

    public String getStringFromPacket(String message) {
        int index = message.indexOf("\n");
        String data = message.substring(0, index);
        return data;
    }

    public int getId(String message) {
        int id, index;
        index = message.indexOf("\n");
        id = Integer.parseInt(message.substring(0, index));
        return id;
    }



    public int getType(String message){
        int type, index;
        index = message.indexOf("\n");
        if(index != -1)
            type = Integer.parseInt(message.substring(0, index));
        else return -1;
        return type;
    }

    public String getData(String message){
        int index = message.indexOf("\n");
        String data = message.substring(index+1);
        return data;
    }

    public String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String encode(String str){
        char[] chars = str.toCharArray();
        for (int i = 0, n = chars.length; i < n; i++) {
            chars[i] =  (char)((int)chars[i] + 5);
        }
        str= new String(chars);
        return str;
    }

    public String decode(String str){
        char[] chars = str.toCharArray();
        for (int i = 0, n = chars.length; i < n; i++) {
            chars[i] =  (char)((int)chars[i] - 5);
        }
        str= new String(chars);
        return str;
    }

    public int getUsrId(byte[] data) {
        String dane = data.toString();
        dane = getData(dane);
        int user_id = Integer.parseInt(dane);
        return user_id;

    }

    public static int getLength(byte[] buffer) {
        String msg = buffer.toString();
        int length = Integer.parseInt(msg.substring(0, msg.indexOf("\n")));

        return length;
    }

    public byte[] getPacketByte(byte[] buffer ) {
        String msg = buffer.toString();
        int index = msg.indexOf("\n");
        msg = msg.substring(index+1);
        return msg.getBytes();
    }

    public void sendOffline(Packet12AddComment packet) {
        MyActivity.connection.send(packet.getPacket());

    }

    public void sendAddUser(Packet2AddUsr packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendNext(Packet8SendNext packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendPing(Packet1Ping packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendRest(Packet14AddRest packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendGetRest(Packet6GetRest packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendAddComment(Packet12AddComment packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendGetComments(Packet10GetComments packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    public void sendDeleteComment(Packet16DelComment packet) {
        MyActivity.connection.send(packet.getPacket());
    }

    static private class ParserTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... params) {

            while (doTheMagic){
                try {
                    signal.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                data = MyActivity.connection.getData();
                Serializer.signal.notify();
            }
            return null;
        }

        protected void onPostExecute(Void resault){
            Connection.destroySignal.notify();
        }
    }


    static private class DestroyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                destroySignal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            doTheMagic = false;
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
