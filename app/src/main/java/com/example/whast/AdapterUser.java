package com.example.whast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterUser extends BaseAdapter {

    private ArrayList<Messeg> meg;
    private Context contex;
    private Resources r;
    private LayoutInflater Inflt;
    private LayoutInflater drap;
    Dialog al;
    boolean tokenImge = true; //sirve para hacer que un boton reacciones de dos formas

    public AdapterUser(ArrayList<Messeg> meg, Context contex,Resources r,LayoutInflater drap) {
        this.meg = meg;
        this.contex = contex;
        this.r = r;
        this.drap = drap;
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

        if (meg.get(position).getTipo() == 'r'){
            if(Inflt == null){
                convertView = LayoutInflater.from(contex).inflate(R.layout.adaptermassegreceptor,null);
            }
            if(convertView == null){
                convertView = Inflt.inflate(R.layout.adaptermassegreceptor,null);
            }else{
                TextView textme = convertView.findViewById(R.id.contmesseg);
                TextView texthors = convertView.findViewById(R.id.timecontn);
                ImageView perfil = convertView.findViewById(R.id.perfilpersonphoto);

                perfil.setImageDrawable(Imageredond(perfil,R.drawable.girl_neut));
                texthors.setText(meg.get(position).getHora());

                //si hay mas itens se ingresara el menssage y los demas itents
                if (meg.get(position).getMeg() != null){
                    textme.setText(meg.get(position).getContenmef());

                }else{ //sino hay itens se ingresala la imagen
                    //si no es un url se ingresara solo el mensaje
                    if(sacarurl(meg.get(position).getContenmef()).equals(" ")){
                        textme.setText(meg.get(position).getContenmef());
                    }else{//si es un url se ingresara una escucha para ingresar a una pagina web
                        textme.setText(meg.get(position).getContenmef());
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(sacarurl(meg.get(position).getContenmef()));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                contex.startActivity(intent);
                            }
                        });
                    }
                    //valida si en caso que no se pase la posicion +1 verificaremos un tipo de respuesta
                    if ((position+1)<=meg.size()-1){
                        if(meg.get(position+1).getTipo() == 's'){
                            convertView.findViewById(R.id.timecontnConte).setVisibility(View.GONE);
                        }
                    }
                }

                //Log.d("url",sacarurl(meg.get(position).getContenmef()));
            }
        }else if (meg.get(position).getTipo() == 'e'){
            if(Inflt == null){
                convertView = LayoutInflater.from(contex).inflate(R.layout.adaptermasegeemisor,null);
            }
            if(convertView == null){
                convertView = Inflt.inflate(R.layout.adaptermasegeemisor,null);
            }else{
                TextView textme = convertView.findViewById(R.id.contmesseg);
                TextView texthors = convertView.findViewById(R.id.timems);
                ImageView perfil = convertView.findViewById(R.id.perfilpersonphoto);

                //perfil.setImageDrawable(Imageredond(perfil,R.drawable.unnamed));
                textme.setText(meg.get(position).getContenmef());
                texthors.setText(meg.get(position).getHora());
            }
        }else if (meg.get(position).getTipo() == 's'){
            if(Inflt == null){
                convertView = LayoutInflater.from(contex).inflate(R.layout.subadapter,null);
            }
            if(convertView == null){
                convertView = Inflt.inflate(R.layout.subadapter,null);
            }else{
                TextView title = convertView.findViewById(R.id.title);
                TextView subtitle = convertView.findViewById(R.id.subtitle);
                TextView texthors = convertView.findViewById(R.id.timecontn);
                TextView money = convertView.findViewById(R.id.money);
                TextView total = convertView.findViewById(R.id.txttotal);
                LinearLayout Ctotal = convertView.findViewById(R.id.total);
                TextView techado = convertView.findViewById(R.id.txttechado);
                LinearLayout Ctechado = convertView.findViewById(R.id.techado);
                TextView dorm = convertView.findViewById(R.id.txtdormitorio);
                LinearLayout Cdorm = convertView.findViewById(R.id.dormitorio);
                TextView baños = convertView.findViewById(R.id.txtbanos);
                LinearLayout Cbaños = convertView.findViewById(R.id.banos);
                TextView estac = convertView.findViewById(R.id.txtestacionamiento);
                LinearLayout Cestac = convertView.findViewById(R.id.estacionamiento);
                ImageView imgCont = convertView.findViewById(R.id.imgcontenedorinfo);

                String cond = (meg.get(position).getMeg().getNombre().length() >= 40) ?meg.get(position).getMeg().getNombre().substring(0,40):meg.get(position).getMeg().getNombre();
                imbuebleEdi ied = meg.get(position).getMeg().getEd();
                title.setText(cond);
                subtitle.setText(meg.get(position).getMeg().getDireccion());
                money.setText(meg.get(position).getMeg().getPrecio());
                texthors.setText(meg.get(position).getHora());

                if (ied.getTotal().equals("")){
                    Ctotal.setVisibility(View.GONE);
                }else {
                    total.setText(ied.getTotal());
                }

                if (ied.getTechado().equals("")){
                    Ctechado.setVisibility(View.GONE);
                }else {
                    techado.setText(ied.getTechado());
                }

                if (ied.getDorm().equals("")){
                    Cdorm.setVisibility(View.GONE);
                }else {
                    dorm.setText(ied.getDorm());
                }

                if (ied.getBaños().equals("")){
                    Cbaños.setVisibility(View.GONE);
                }else {
                    baños.setText(ied.getBaños());
                }

                if (ied.getEstac().equals("")){
                    Cestac.setVisibility(View.GONE);
                }else {
                    estac.setText(ied.getEstac());
                }

                View finalConvertView = convertView;
                convertView.findViewById(R.id.btnImageConte).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tokenImge){
                            finalConvertView.findViewById(R.id.imagecontenrefe).setVisibility(View.VISIBLE);
                        }else{
                            finalConvertView.findViewById(R.id.imagecontenrefe).setVisibility(View.GONE);
                        }
                        tokenImge = !tokenImge;
                    }
                });

                final int[] contadorExt = {0};

                Picasso.get()
                        .load(meg.get(position).getMeg().getURLSI().get(contadorExt[0]))
                        //.placeholder(R.drawable.user_placeholder)
                        //.error(R.drawable.user_placeholder_error)
                        .into(imgCont);

                convertView.findViewById(R.id.izquiecontenedorinfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contadorExt[0]--;

                        Picasso.get()
                                .load(meg.get(position).getMeg().getURLSI().get(contadorExt[0]))
                                //.placeholder(R.drawable.user_placeholder)
                                //.error(R.drawable.user_placeholder_error)
                                .into(imgCont);

                        if(contadorExt[0] == 0){
                            contadorExt[0]= meg.get(position).getMeg().getURLSI().size()-1;
                        }

                    }
                });

                convertView.findViewById(R.id.derechcontenedorinfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contadorExt[0]++;
                        Picasso.get()
                                .load(meg.get(position).getMeg().getURLSI().get(contadorExt[0]))
                                //.placeholder(R.drawable.user_placeholder)
                                //.error(R.drawable.user_placeholder_error)
                                .into(imgCont);
                        if(contadorExt[0] == meg.get(position).getMeg().getURLSI().size()-1){
                            contadorExt[0]= 0;
                        }
                    }
                });

                convertView.findViewById(R.id.contenedorinfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(meg.get(position).getMeg().getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        contex.startActivity(intent);
                    }
                });

                convertView.findViewById(R.id.redireccionadorprin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(meg.get(position).getMeg().getUrlgenera());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        contex.startActivity(intent);
                    }
                });
                if ((position+1)<=meg.size()-1){
                    if(meg.get(position+1).getTipo() == 's'){
                        convertView.findViewById(R.id.redireccionadorprin).setVisibility(View.GONE);
                        convertView.findViewById(R.id.timecontnConte).setVisibility(View.GONE);
                    }
                }
            }
        }
        return convertView;
    }

    public String sacarurl(String meeg){
        char[] lst = meeg.toCharArray();
        int abrebo = 0;
        for (int i = 0; i< lst.length-1; i++){
            if(lst[i]=='h'){
                abrebo = i;
                break;
            }
        }
       return (abrebo!=0)?meeg.substring(abrebo,lst.length-1):" ";
    }

    public RoundedBitmapDrawable Imageredond(ImageView img, Integer codeImg){
        //rediseñar el Image view
        Bitmap bitm = BitmapFactory.decodeResource(this.r, codeImg);
        Bitmap bm3 = null;

        if (img.getLayoutParams().width == 0){
            bm3 = Bitmap.createScaledBitmap(bitm, 200, 200, true);
        }else{
            bm3 = Bitmap.createScaledBitmap(bitm, img.getLayoutParams().width + 200, img.getLayoutParams().height + 200, true);
        }

        RoundedBitmapDrawable imag = RoundedBitmapDrawableFactory.create(this.r, bm3);
        imag.setCircular(true);
        return imag;
    }
}
