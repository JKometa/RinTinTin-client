package com.example.TinTin;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Modzel
 * Date: 27.05.13
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */

public class LazyAdapterRes extends BaseAdapter {

    private Activity activity;
    private ArrayList<Res> data;
    private static LayoutInflater inflater = null;


    public LazyAdapterRes(Activity a, ArrayList<Res> lista) {
        activity = a;
        data = lista;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView name = (TextView) vi.findViewById(R.id.loginy);
        TextView adress = (TextView) vi.findViewById(R.id.data);
        TextView type = (TextView) vi.findViewById(R.id.comm);

        name.setText(MyActivity.restauracje.getListaRes().get(position).nazwa);
        adress.setText(MyActivity.restauracje.getListaRes().get(position).adres);
        type.setText(MyActivity.restauracje.getListaRes().get(position).typ);


        Res current = data.get(position);


        return vi;
    }
}