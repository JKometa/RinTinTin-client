package com.example.TinTin;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

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

    int type = 0;
    private ProgressDialog progressDialog2;
    boolean isShowing = false;
    public static boolean running;
    static public  Object signal = new Object();
    static public  Object destroySignal = new Object();
    private NetTask net;
    private DestroyTask destroy;
    private boolean doTheMagic = true;
    private static RecivedObject recivedObject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poilist);

        running = true;
        list = (ListView) findViewById(R.id.list);

        Bundle extras = getIntent().getExtras();
        idRes = extras.getInt("resId");

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this, MyActivity.restauracje.getListaOnlineId(idRes));

        list.setAdapter(adapter);




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
                                MyActivity.serializer.sendComment(MyActivity.restauracje.getUsrId().user_id, idRes, dataDoBazy, opis);


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
                MyActivity.serializer.getComments(idRes, commentDate);



                    if(MyActivity.connection.getConnectionState()){
                        new downloadAsyncTask().execute();;

                        //MyActivity.restauracje.sruOnline();
                    }
                    else{
                        Toast.makeText(CustomizedListView.this, "Brak polaczenia", Toast.LENGTH_LONG).show();
                    }

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
                            MyActivity.serializer.deleteComment(MyActivity.restauracje.getListaOnline().get(position).id);

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

    private class downloadAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomizedListView.this);
            progressDialog.setTitle("Pobieram restauracje ");
            progressDialog.setMessage("Proszę chwilę poczekać...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            isShowing = true;
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            MyActivity.restauracje.sruRes();
            MyActivity.serializer.getRest();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            while (true){
                if((type == 9)) {

                    isShowing = false;
                    type = 0;
                    break;

                }
            }

            adapter.notifyDataSetChanged();
            progressDialog.dismiss();

        }

    }

    protected void onDestroy(){
        super.onDestroy();
        //MyActivity.restauracje.sruOnline();

    }

    public void onResume(){
        super.onResume();
        running = true;

    }

    public void onPause(){
        super.onPause();
        running = false;
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
                if(recivedObject.getId() == 9)
                    type = 9;
                MyActivity.actionSwitcher(recivedObject);
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