package com.example.TinTin;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 27.05.13
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */

public class CustomizedListViewRes extends Activity {


    ListView list;
    LazyAdapterRes adapter;
    String nazwa, typ, adres;
    private ProgressDialog progressDialog2;
    int type = 0;
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
        Baza restauracje = new Baza(getApplicationContext());


        try {

            restauracje.createDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapterRes(this, restauracje.getListaRes());
        Log.d("results", "2");
        list.setAdapter(adapter);
        Log.d("results", "3");


        Button dodaj = (Button) findViewById(R.id.dodaj);
        Button pobierz = (Button) findViewById(R.id.pobierz);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomizedListViewRes.this);
                // Get the layout inflater
                LayoutInflater inflater = CustomizedListViewRes.this.getLayoutInflater();

                View popUpView = getLayoutInflater().inflate(R.layout.dodajres, null);
                final EditText nazwaTex = (EditText) popUpView.findViewById(R.id.nazwa);
                final EditText adresText = (EditText) popUpView.findViewById(R.id.adres);
                final EditText typText = (EditText) popUpView.findViewById(R.id.typ);

                //typText.setText("dupa");
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(popUpView)
                        // Add action buttons
                        .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                nazwa = nazwaTex.getText().toString();
                                adres = adresText.getText().toString();
                                typ = typText.getText().toString();
                                MyActivity.serializer.sendRest(nazwa, adres, typ);


                                int index = MyActivity.restauracje.getListaRes().size();
                                MyActivity.restauracje.addRes(index, adres, nazwa, typ);

                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();



            }
        });

        pobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new downloadAsyncTask().execute();

            }
        });



        // Click event for single list row

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), CustomizedListView.class);
                intent.putExtra("resId", position-1);
                startActivity(intent);

            }
        });
        adapter.notifyDataSetChanged();
    }


    private class downloadAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomizedListViewRes.this);
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