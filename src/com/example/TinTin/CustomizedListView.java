package com.example.TinTin;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 27.05.13
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */

public class CustomizedListView extends Activity {


    ListView list;
    LazyAdapter adapter;
    String opis;
    int idRes;
    long date;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    int type = 0;
    private ProgressDialog progressDialog2;
    boolean isShowing = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poilist);


        list = (ListView) findViewById(R.id.list);

        Bundle extras = getIntent().getExtras();
        idRes = extras.getInt("resId");

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this, MyActivity.restauracje.getListaOnlineId(idRes));
        Log.d("results", "2");
        list.setAdapter(adapter);
        Log.d("results", "3");
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    byte[] buffer = new byte[60];
                    try {
                        if(MyActivity.input != null &&  MyActivity.input.available() > 0)
                            MyActivity.input.read(buffer);
                        if(buffer != null)
                            type = MyActivity.parsePacket(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if(type == 9) {
                        progressDialog2.dismiss();
                        isShowing = false;
                        type = 0;


                    }
                }
            }
        });
        listener.start();


        Button dodaj = (Button) findViewById(R.id.dodaj);
        Button pobierz = (Button) findViewById(R.id.pobierz);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomizedListView.this);
                // Get the layout inflater
                LayoutInflater inflater = CustomizedListView.this.getLayoutInflater();
                View popUpView = getLayoutInflater().inflate(R.layout.dodaj, null);
                final EditText commentText = (EditText) popUpView.findViewById(R.id.komentarz);


                date = System.currentTimeMillis();
                Date nowaData = new Date(date);
                final String dataDoBazy = nowaData.toString();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(popUpView)
                        // Add action buttons
                        .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                opis = commentText.getText().toString();
                                Log.d("Komentarz", opis);
                                byte[] wyslij = new byte[0];

                                wyslij = (12 + "\n" + MyActivity.restauracje.getUsrId().user_id + "\n" + idRes+1 + "\n" + dataDoBazy + "\n" + opis + "\n").getBytes(UTF8_CHARSET);

                                Log.d("Komentarz", (12 + "\n" + MyActivity.restauracje.getUsrId().user_id + "\n" + idRes + "\n" + date + "\n" + opis + "\n"));
                                if (MyActivity.output != null) {
                                    try {
                                        MyActivity.output.write(wyslij);
                                        MyActivity.output.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    }

                                } else
                                    MyActivity.restauracje.add(MyActivity.restauracje.getUsrId().user_id, idRes, opis, dataDoBazy);

                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }

                        }).setNeutralButton("Wyślij offline", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        MyActivity.restauracje.add(MyActivity.restauracje.getUsrId().user_id, idRes, opis, dataDoBazy);
                        }
                    });
                builder.show();




            }
        });

        pobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.restauracje.sruOnline();
                int index = MyActivity.restauracje.getListaOnline().size();
                long commentDate = 0;
                Date nowaData = new Date();
                if(index != 0){
                    DateFormat df = new DateFormat();
                    df.format("yyyy-MM-ddThh:mm:ss", new Date());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    String sup = (MyActivity.restauracje.getListaOnline().get(index -1)).date;
                    try {
                        nowaData = format.parse(sup);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                     if(nowaData!=null)
                        commentDate = nowaData.getTime();
                }
                else{
                    commentDate = 0;
                }
                byte[] wyslij = (10+"\n"+idRes+"\n"+commentDate+"\n").getBytes();

                try {
                    if(MyActivity.output != null){
                        progressDialog2 = new ProgressDialog(CustomizedListView.this);
                        progressDialog2.setTitle("Pobieram komentarze ");
                        progressDialog2.setMessage("Proszę chwilę poczekać...");
                        progressDialog2.setIndeterminate(true);
                        progressDialog2.setCancelable(false);
                        isShowing = true;
                        progressDialog2.show();
                        MyActivity.output.write(wyslij);
                        MyActivity.output.flush();

                        //MyActivity.restauracje.sruOnline();
                    }
                    else{
                        Toast.makeText(CustomizedListView.this, "Brak polaczenia", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                adapter.notifyDataSetChanged();
            }


        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if(MyActivity.restauracje.getListaOnline().get(position).id == MyActivity.restauracje.getUsrId().id){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomizedListView.this);
                    // Get the layout inflater
                    LayoutInflater inflater = CustomizedListView.this.getLayoutInflater();

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int id) {
                            byte[] wyslij = (16 + "\n" + MyActivity.restauracje.getListaOnline().get(position).id+"\n").getBytes();

                            try {
                                if(MyActivity.output != null){
                                    MyActivity.output.write(wyslij);
                                    MyActivity.output.flush();
                                }
                                else{
                                    Toast.makeText(CustomizedListView.this, "Brak polaczenia", Toast.LENGTH_LONG).show();
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
        });

    }

    protected void onDestroy(){
        super.onDestroy();
        //MyActivity.restauracje.sruOnline();

    }
}