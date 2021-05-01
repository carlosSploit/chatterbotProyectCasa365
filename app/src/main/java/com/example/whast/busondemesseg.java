package com.example.whast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class busondemesseg extends AppCompatActivity {

    private Toolbar bar;
    private ImageView perfil;
    private ImageView iaperfil;
    private GridView contenMesseg;
    private ArrayList<Messeg> messeg;
    private EditText edmes;
    RequestQueue  rq;
    TexttoSpeach tts;
    Handler handler=new Handler();
    AnimationDrawable ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busondemesseg);
        //declaracion de variables
        bar = (Toolbar)findViewById(R.id.my_tolbarmesf);
        contenMesseg = (GridView)findViewById(R.id.contenmeg);
        edmes = (EditText)findViewById(R.id.mesag);
        iaperfil = (ImageView)findViewById(R.id.iaperfil);
        tts=new TexttoSpeach(getApplicationContext());
        setSupportActionBar(bar);

        //incertando petfil
        iaperfil.setBackground(getResources().getDrawable(R.drawable.girl_neut));

        //Inicializar la libreria de voley para consulyas api res
        rq = Volley.newRequestQueue(this);

        contenMesseg.smoothScrollToPosition(View.SCROLL_INDICATOR_END);

        messeg = new ArrayList<>();
        messeg.add(new Messeg("Hola me llamo Sofia, y sere tu asistente en estos momentos",'r',horsnow(),null));
        AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
        contenMesseg.setAdapter(ad);

        animation_vos("Hola me llamo Sofia, y sere tu asistente en estos momentos");

        Toast.makeText(getApplicationContext(),"Precionarar la flecha para troceder, y para añadir un mensajer" +
                "escriba en la casilla de color blanco y precione la flecha del costado con el teclado minimizado" +
                "",Toast.LENGTH_LONG).show();

    }

    public void emviasmesseg(View view){
        messeg.add(new Messeg(edmes.getText().toString(),'e',horsnow(),null));
        AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
        contenMesseg.setAdapter(ad);
        ResponsedIA(edmes.getText().toString());
        edmes.setText("");
    }

    public void ResponsedIA(String Apiparent){
        StringRequest st=new StringRequest(
                Request.Method.GET,
                "https://chatterbotte.herokuapp.com/app/chatbot/" + Apiparent.replaceAll(" ", "%20")
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.has("alquiler")||obj.has("Comprar")||obj.has("Recomend")||obj.has("Favorit")){
                        ArrayList<inmuebleM> lsms=new ArrayList<>();
                        JSONObject json=null;

                        if(obj.has("alquiler")){
                            json = obj.getJSONObject("alquiler");
                        }else if(obj.has("Comprar")){
                            json = obj.getJSONObject("Comprar");
                        }else if(obj.has("Recomend")){
                            json = obj.getJSONObject("Recomend");
                        }else if(obj.has("Favorit")){
                            json = obj.getJSONObject("Favorit");
                        }


                        Iterator x = json.keys();

                        messeg.add(new Messeg(obj.getString("messeg"),'r',horsnow(),null));
                        int cont = 0;
                        while (x.hasNext()){
                            if(cont>4){break;} //para salir del whilw
                            lsms.clear();
                            String key = (String) x.next();
                            inmuebleM ms = new inmuebleM(
                                    json.getJSONObject(key).getString("depart"),
                                    json.getJSONObject(key).getString("costo"),
                                    json.getJSONObject(key).getString("result"),
                                    json.getJSONObject(key).getString("url"),
                                    json.getJSONObject(key).getString("urlg")
                            );
                            ms.setEd(stracSpesifi(json.getJSONObject(key).getString("espe")));
                            ms.setUrl(StracImg(json.getJSONObject(key).getString("img")));
                            lsms.add(ms);
                            messeg.add(new Messeg(obj.getString("messeg"),'s',horsnow(),ms));
                            cont++;
                        }
                        //Log.d("JSONarray","Result: "+ lsms.size());
                    }else{
                        messeg.add(new Messeg(obj.getString("messeg"),'r',horsnow(),null));
                    }
                    AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
                    contenMesseg.setAdapter(ad);
                    contenMesseg.smoothScrollToPosition(View.SCROLL_INDICATOR_END); //vajar el scroll
                    //Log.d("Messeg;","leng:"+ String.valueOf(obj.getString("messeg")).length()+" text: "+obj.getString("messeg"));
                    animation_vos(obj.getString("messeg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorVoley",error.getMessage());
            }
        });
        rq.add(st);
    }

    public imbuebleEdi stracSpesifi(String code){
        imbuebleEdi ied = null;
        String  total = "";
        String  techado= "";
        String  dorm= "";
        String baños= "";
        String estac= "";
        try {
            JSONObject obj = new JSONObject(code);
            if (obj.has("total")){
                total = obj.getString("total");
            }
            if (obj.has("techado")){
                techado = obj.getString("techado");
            }
            if (obj.has("dorm.")){
                dorm = obj.getString("dorm.");
            }
            if (obj.has("baños")){
                baños = obj.getString("baños");
            }
            if (obj.has("estac.")){
                estac = obj.getString("estac.");
            }
            ied = new imbuebleEdi(total,techado,dorm,baños,estac);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ied;
    }

    public ArrayList<String> StracImg(String code){
        ArrayList<String> imgs = new ArrayList<>();
        try {
            JSONObject json =  new JSONObject(code);
            Iterator x = json.keys();
            while (x.hasNext()){
                String key = (String) x.next();
                imgs.add(json.getString(key));
            }
        } catch (JSONException e) {
            Log.e("resultadodimgError","A susedio un error al tratar de inprimir");
        }
        return imgs;
    }

    public void animation_vos(String messeg){
        int valorEntero = (int) Math.floor(Math.random()*(2-0+1));
        Log.d("Animacion:","resulta "+valorEntero);

        char[] lts = messeg.toCharArray();
        Integer[] listIm = {R.drawable.animationsinbrazos,R.drawable.animationbrazo1,R.drawable.animationbrazo};

        redimentImageView(valorEntero);

        //lanzar animacion
        iaperfil.setBackgroundResource(listIm[valorEntero]);
        AnimationDrawable ad = (AnimationDrawable) iaperfil.getBackground();
        ad.start();

        tts.speekOut(messeg);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ad.stop();
                redimentImageView(0);
                iaperfil.setBackground(getResources().getDrawable(R.drawable.girl_neut));
            }
        },lts.length * 70);
    }

    //evitar el cambio grotesco entre animacion y animacion
    private void redimentImageView(int classAnin){
        int ancho = 0;
        int alto = 0;
        ArrayList<Integer> redis=new ArrayList<>();
        redis.add(1);
        ArrayList<Integer> stati=new ArrayList<>();
        stati.add(0);
        stati.add(2);
        if(stati.indexOf(classAnin)!=-1){
            ancho = 500;
            alto = 600;
        }else if(redis.indexOf(classAnin)!=-1){
            ancho = 650;
            alto = 600;
        }
        //redimencionar el imagevie
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ancho, alto);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        iaperfil.setLayoutParams(params);
    }

    private String horsnow(){
        Time t=new Time(Time.getCurrentTimezone());
        t.setToNow();
        int hors = t.hour;
        int mins = t.minute;
        return ((String.valueOf(hors).length()>1)?String.valueOf(hors):"0"+String.valueOf(hors))
                + ":"
                + ((String.valueOf(mins).length()>1)?String.valueOf(mins):"0"+String.valueOf(mins));
    }

    //enlasar el siño del menu al toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.mymenumesseg,menu);
        return super.onCreateOptionsMenu(menu);
    }

}