package com.example.TinTin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 27.05.13
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public class CommentList extends Activity {

    private Baza restauracja;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PoiList.lista = new ArrayList<Komentarz>();
        Intent intent = new Intent(this, CustomizedListView.class);

        startActivity(intent);

        //   restauracja = new Poi("BlowUp Hall5050", true,true,true,true,false,false,true,true,true,true,false);
         /*
        for(int i = 0; i<10;++i) {
           PoiList.lista.add( new Poi("BlowUp Hall5050" + i, true ,true,true,true,true,true,true,true,true,true,true));
        }

                     */




    }

    public void showPoiList(View view) {
        // Toast.makeText(this, "Echo!\n" + restauracja.name, 1000).show();
        Intent intent = new Intent(this, CustomizedListView.class);

        startActivity(intent);


    }
}
