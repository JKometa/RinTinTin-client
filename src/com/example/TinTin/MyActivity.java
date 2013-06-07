package com.example.TinTin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MyActivity extends Activity{


    private String serverIpAddress;
    //private InetAddress adress;
    private Socket socket;
    private int port;
    private Handler socketHandler;
    static public DataInputStream input;
    static public DataOutputStream output;
    private String comment, newLogin, haslo;
    private InetAddress adress = null;
    static public Baza restauracje;
    private static Parser parser;
   // private OutputStream output;
   // private InputStream input;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        parser = new Parser();
        restauracje = new Baza(getApplicationContext());


        try {

            restauracje.createDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }


        serverIpAddress = "185.5.99.132";
        port = 10001;
        PoiList.lista = new ArrayList<Komentarz>();
        PoiList.lista = restauracje.getLista();
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    adress = InetAddress.getByName(serverIpAddress);
                } catch (UnknownHostException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                if (adress != null) {
                    try {
                        socket = new Socket(adress, port);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

                try {
                    if(socket != null)
                    input = new DataInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                try {
                    if(socket != null)
                    output = new DataOutputStream(socket.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if(socket != null)
                    if(socket.isConnected())
                     Log.d("*msg", "podlaczony");
                    //Toast.makeText(MyActivity.this, "provider enabled", Toast.LENGTH_SHORT).show();

                try{
                     //This line maybe throw an error like timeout

                    while (true) {
                        try {
                            byte[] buffer = new byte[300];
                            if(input != null && input.available() > 0){
                                input.read(buffer);

                                if(buffer != null)
                                    parsePacket(buffer);
                            }




//                            String hello = "Hello Sczalek!!!";
//                            byte[] wyslij = hello.getBytes();
//                            output.write(wyslij);
//
//                            output.flush();
//                            Thread.sleep(3000);
                            //check not null

                        } catch (IOException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }


                    }


                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        connection.start();


        Button listaRes = (Button) findViewById(R.id.listaRes);

        Button podlacz = (Button) findViewById(R.id.rejestracja);

        Button wyslij = (Button) findViewById(R.id.sendOffline);

        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restauracje.getLista().size() > 1)
                    sendOffline();
                else{
                    Toast.makeText(MyActivity.this, "Brak komentarzy do wysłania", Toast.LENGTH_LONG);
                }
            }
        });

        listaRes.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, CustomizedListViewRes.class);
                startActivity(intent);

            }
        });




        podlacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int login = restauracje.getUsrId().id;
                if( login == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = MyActivity.this.getLayoutInflater();
                View popUpView = getLayoutInflater().inflate(R.layout.rejestracja, null);

                final EditText loginText = (EditText) popUpView.findViewById(R.id.login);
                final EditText hasloText = (EditText) popUpView.findViewById(R.id.haslo);



                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(popUpView)
                        // Add action buttons
                        .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                newLogin = loginText.getText().toString();
                                haslo = hasloText.getText().toString();
                                haslo = parser.md5(haslo);
                                byte[] wyslij = (2+"\n"+newLogin+"\n"+haslo+"\n").getBytes();
                                try {
                                    if(output != null){
                                        output.write(wyslij);
                                        output.flush();
                                    }
                                    else{
                                        Toast.makeText(MyActivity.this, "Brak polaczenia", Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();



            }
                else{
                    Toast.makeText(MyActivity.this, "Użytkownik jest już zarejestrowany", Toast.LENGTH_LONG).show();
                }

        }});


    }

    public void onResume(){
        super.onResume();
        final int login = restauracje.getUsrId().id;
        if( login == -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
            // Get the layout inflater
            LayoutInflater inflater = MyActivity.this.getLayoutInflater();
            View popUpView = getLayoutInflater().inflate(R.layout.rejestracja, null);

            final EditText loginText = (EditText) popUpView.findViewById(R.id.login);
            final EditText hasloText = (EditText) popUpView.findViewById(R.id.haslo);



            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(popUpView)
                    // Add action buttons
                    .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            newLogin = loginText.getText().toString();
                            haslo = hasloText.getText().toString();
                            haslo = parser.md5(haslo);
                            byte[] wyslij = (2+"\n"+newLogin+"\n"+haslo+"\n").getBytes();
                            try {
                                if(output != null){
                                    output.write(wyslij);
                                    output.flush();
                                }
                                else{
                                    Toast.makeText(MyActivity.this, "Brak polaczenia", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    })
                    .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();




        }

    }

    void commentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = MyActivity.this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dodaj, null))
                // Add action buttons
                .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        View popUpView = getLayoutInflater().inflate(R.layout.dodaj, null);
                        EditText textEdit = (EditText) popUpView.findViewById(R.id.komentarz);
                        comment = textEdit.toString();
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }



    public void sendComment(String comment){
        //output.write(comment);
       // output.flush();
    }

    static public int parsePacket(byte[] data){
        String message = null;
        try {
            message = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        int type = parser.getType(message);
        //Log.d("odSerwera", "TYP PAKIETU:   " + Integer.toString(type));
        if(type != -1)
            message = parser.getData(message);

        switch (type){
            case -1:
                break;
            case 0:
                Log.d("odSerwera", message);
                pong(message);
                break;
            case 1:
                //moj pakiet
                break;
            case 2:
                //moj pakiet
                break;
            case 3:
                Log.d("odSerwera", message);
                handleRegistration(message);
                break;
            case 4:
                //moj pakiet
                break;
            case 5:
                Log.d("odSerwera", message);
                handleLastRestaurant(message);
                break;
            case 6:
                //moj pakiet
                break;
            case 7:
                Log.d("odSerwera", message);
                String[] restaurant = parser.handleGetRestaurants(message);
                addNewRestaurant(restaurant);
                break;
            case 8:
                //moj pakiet
                break;
            case 9:
                Log.d("odSerwera", message);
                return type;
            case 10:
                //moj pakiet
                break;
            case 11:
                Log.d("odSerwera", message);
                String[] newComment = parser.handleGetComments(message);
                Log.d("Komentarz", message);
                addNewComment(newComment);
                break;
            case 12:
                //moj pakiet
                break;
            case 13:
                Log.d("odSerwera", message);
                parser.hanldeAddComment(message);
                break;
            case 14:
                //moj pakiet
                break;
            case 15:
                Log.d("odSerwera", message);
                int restId = parser.handleAddRestaurant(message);
                addRestId(restId);
                break;
            case 16:
                //moj pakiet
                break;
            case 17:
                Log.d("odSerwera", message);
                parser.handleDeleteComment(message);
                break;
        }
      return type;
    }

    private void sendOffline(){
        ArrayList<Komentarz> list = restauracje.getLista();
        for(Komentarz k: list){
            byte[] wyslij = (12 + "\n" + MyActivity.restauracje.getLista().get(0).user_id + "\n"
                    + k.res_id + "\n" + k.date + "\n" + k.text + "\n").getBytes();

            if(MyActivity.output != null){
                try {
                    MyActivity.output.write(wyslij);
                    MyActivity.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
        }
        Komentarz user = restauracje.getUsrId();
        restauracje.sru();
        restauracje.add(user.user_id, user.res_id, user.text, user.date);
    }

    private static void wiecej(){
        byte[] wyslij = (8+"\n").getBytes();

        try {
            if(output != null){
                output.write(wyslij);
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    private static void addRestId(int restId) {
        int index = restauracje.getListaRes().size();
        String srch = restauracje.getListaRes().get(index -1).nazwa;
        restauracje.resUpdate(srch, restId);
    }

    private static void addNewComment(String[] newComment) {
        restauracje.addOnline(1,1, newComment[1], newComment[2], 1);
        byte[] wyslij = (8+"\n").getBytes();

        try {
            if(output != null){
                output.write(wyslij);
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void addNewRestaurant(String[] restaurant) {
        restauracje.addRes(Integer.parseInt(restaurant[0]), restaurant[1], restaurant[2], restaurant[3]);
        byte[] wyslij = (8+"\n").getBytes();

        try {
            if(output != null){
                output.write(wyslij);
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void pong(String message) {
        if(message != null){
            byte[] wyslij = (1+"\n"+restauracje.getLista().get(0).user_id+"\n").getBytes();

            try {
                if(output != null){
                    output.write(wyslij);
                    output.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    public static void handleLastRestaurant(String message) {
        if(message != null){
            String data = parser.getStringFromPacket(message);
            int index = restauracje.getListaRes().size();
            index--;
            if(!(data.equals(restauracje.getListaRes().get(index)))){
                index++;
                byte[] buffer = (6+"\n"+index+"\n").getBytes();
                try {
                    if(output != null){
                        output.write(buffer);
                        output.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static void handleRegistration(String message) {
        if(message != null){
            String id = parser.getStringFromPacket(message);
            restauracje.komUpdateId(restauracje.getUsrId().text, Integer.parseInt(id));
        }
    }

}
