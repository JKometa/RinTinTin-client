package com.example.TinTin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class MyActivity extends Activity{


    private String serverIpAddress;
    //private InetAddress adress;
    private Socket socket;
    private int port;
    private String comment, newLogin, haslo;
    private InetAddress adress = null;
    static public Baza restauracje;
    public static Parser parser;
    static public  Object signal = new Object();
    static public  Object destroySignal = new Object();
    private boolean doTheMagic = true;
    public static Connection connection;
    public static Serializer serializer;
    private static RecivedObject recivedObject;
    private NetTask net;
    private DestroyTask destroy;
    public static boolean running;
   // private OutputStream output;
   // private InputStream input;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        running = true;
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


        serializer = new Serializer();
        parser = new Parser();
        connection = new Connection(serverIpAddress, port);




        net = new NetTask();
        net.execute();
        destroy = new DestroyTask();
        destroy.execute();





        Button listaRes = (Button) findViewById(R.id.listaRes);

        Button podlacz = (Button) findViewById(R.id.rejestracja);

        Button wyslij = (Button) findViewById(R.id.sendOffline);

        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restauracje.getLista().size() > 1)
                    serializer.sendOffline();
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
                                serializer.sendAddUser(newLogin, haslo);
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
        running = true;

    }

    public void onPause(){
        super.onPause();
        running = false;
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

    static public int actionSwitcher(RecivedObject data){

        int type = recivedObject.getId();



        switch (type){
            case -1:
                break;
            case 0:
//                Log.d("odSerwera", message);
//                pong(message);
                break;
            case 1:
                //moj pakiet
                break;
            case 2:
                //moj pakiet
                break;
            case 3:
//                Log.d("odSerwera", message);
                handleRegistration(recivedObject.getData()[0]);
                break;
            case 4:
                //moj pakiet
                break;
            case 5:
//                Log.d("odSerwera", message);
//                handleLastRestaurant(message);
                break;
            case 6:
                //moj pakiet
                break;
            case 7:
//                Log.d("odSerwera", message);
                addNewRestaurant(recivedObject.getData());
                break;
            case 8:
                //moj pakiet
                break;
            case 9:

                return type;
            case 10:
                //moj pakiet
                break;
            case 11:
                addNewComment(recivedObject.getData());
                break;
            case 12:
                //moj pakiet
                break;
            case 13:
//                Log.d("odSerwera", message);
//                parser.hanldeAddComment(message);
                break;
            case 14:
                //moj pakiet
                break;
            case 15:
//                Log.d("odSerwera", message);
                int restId = Integer.parseInt(recivedObject.getData()[0]);
                addRestId(restId);
                break;
            case 16:
                //moj pakiet
                break;
            case 17:
//                Log.d("odSerwera", message);

                boolean resault = Boolean.parseBoolean(recivedObject.getData()[0]);
                break;
        }
      return type;
    }



    private static void sendNext(){

        serializer.sendNext();

    }
    private static void addRestId(int restId) {
        int index = restauracje.getListaRes().size();
        String srch = restauracje.getListaRes().get(index -1).nazwa;
        restauracje.resUpdate(srch, restId);
    }

    private static void addNewComment(String[] newComment) {
        restauracje.addOnline(1,1, newComment[1], newComment[2], 1);
        sendNext();
    }

    private static void addNewRestaurant(String[] restaurant) {
        restauracje.addRes(Integer.parseInt(restaurant[0]), restaurant[1], restaurant[2], restaurant[3]);
        sendNext();
    }

    private static void pong(String message) {
        if(message != null){
            serializer.sendPong();


        }
    }

    public static void handleLastRestaurant(String message) {
        if(message != null){
            String data = parser.getStringFromPacket(message);
            int index = restauracje.getListaRes().size();
            index--;
//            if(!(data.equals(restauracje.getListaRes().get(index)))){
//                index++;
//                byte[] buffer = (6+"\n"+index+"\n").getBytes();
//                try {
//                    if(output != null){
//                        output.write(buffer);
//                        output.flush();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//            }
        }
    }

    public static void handleRegistration(String message) {
        if(message != null){
            String id = parser.getStringFromPacket(message);
            restauracje.komUpdateId(restauracje.getUsrId().text, Integer.parseInt(id));
        }
    }

    private class NetTask extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... params) {

               while(doTheMagic){
                   try {
                       signal.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                   }
                   recivedObject = Serializer.reciveOject();
                   actionSwitcher(recivedObject);
               }


            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        protected void onPostExecute(Void resault){

        }
    }

    private class DestroyTask extends AsyncTask<Void, Void, Void>{

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
