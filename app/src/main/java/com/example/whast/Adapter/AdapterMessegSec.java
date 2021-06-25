package com.example.whast.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.whast.R;
import com.example.whast.Clases.inmuebleM;

import java.util.ArrayList;

public class AdapterMessegSec extends BaseAdapter {

    private ArrayList<inmuebleM> meg;
    private Context contex;
    private Resources r;
    private LayoutInflater Inflt;

    public AdapterMessegSec(ArrayList<inmuebleM> meg, Context contex, Resources r) {
        this.meg = meg;
        this.contex = contex;
        this.r = r;
    }

    @Override
    public int getCount() {
        return meg.size();
    }

    @Override
    public Object getItem(int position) {
        return meg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position <= 4){
            if(Inflt == null){
                convertView = LayoutInflater.from(contex).inflate(R.layout.subadapter,null);
            }
            if(convertView == null){
                convertView = Inflt.inflate(R.layout.subadapter,null);
            }else{
                TextView title = convertView.findViewById(R.id.title);
                TextView subtitle = convertView.findViewById(R.id.subtitle);
                TextView money = convertView.findViewById(R.id.money);
                String cond = (meg.get(position).getNombre().length() >= 20) ?meg.get(position).getNombre().substring(0,20):meg.get(position).getNombre();

                title.setText(cond);
                subtitle.setText(meg.get(position).getDireccion());
                money.setText(meg.get(position).getPrecio());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(meg.get(position).getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        contex.startActivity(intent);
                    }
                });
            }
        }
        return convertView;
    }
}
