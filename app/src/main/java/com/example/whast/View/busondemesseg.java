package com.example.whast.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.whast.login;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whast.Adapter.AdapterUser;
import com.example.whast.Clases.Messeg;
import com.example.whast.Clases.imbuebleEdi;
import com.example.whast.Clases.inmuebleM;
import com.example.whast.Complements.CircleTransform;
import com.example.whast.Complements.TexttoSpeach;
import com.example.whast.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class busondemesseg extends AppCompatActivity {

    private Toolbar bar;
    private ImageView perfil;
    private ImageView iaperfil;
    private ImageButton img;
    private ImageButton anim;
    private LinearLayout animcarga;
    private GridView contenMesseg;
    private ArrayList<Messeg> messeg;
    private EditText edmes;
    private TextView nombre;
    RequestQueue  rq;
    TexttoSpeach tts;
    Handler handler=new Handler();
    AnimationDrawable ad;
    AnimationDrawable animacion;
    AlertDialog alert;
    public static final int MY_DEFAULT_TIMEOUT = 150000;
    private FirebaseAuth auth;
    private static final int REQ_CODE = 100;
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busondemesseg);
        //declaracion de variables
        bar = (Toolbar)findViewById(R.id.my_tolbarmesf);
        contenMesseg = (GridView)findViewById(R.id.contenmeg);
        edmes = (EditText)findViewById(R.id.mesag);
        iaperfil = (ImageView)findViewById(R.id.iaperfil);
        img = (ImageButton)findViewById(R.id.Enviarmenss);
        animcarga = (LinearLayout)findViewById(R.id.animascarga);
        anim = (ImageButton)findViewById(R.id.imageanimacion);
        tts=new TexttoSpeach(getApplicationContext());

        perfil = (ImageView)findViewById(R.id.imageuser);
        nombre = (TextView)findViewById(R.id.nombreuser);

        setSupportActionBar(bar);

        animcarga.setVisibility(View.GONE);

        //inicializando datos de login
        auth = FirebaseAuth.getInstance();
        FirebaseUser cuFirebaseUser = auth.getCurrentUser();

        //Configurar las gso para google signIn con el fin de luego desloguear de google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        String[] nomaux = cuFirebaseUser.getDisplayName().split(" ");
        String nom = nomaux[0];
        nombre.setText(nom);

        Picasso.get()
                .load(cuFirebaseUser.getPhotoUrl())
                .transform(new CircleTransform())
                .into(perfil);
        //inicializar accion del boton
        img.setImageResource(R.drawable.ic_baseline_mic_none_24);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entradadevoz();
            }
        });

        //escucha para cambio de comportamiento del boton
        edmes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start != 0){

                    img.setImageResource(R.drawable.send);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //enviar un mensaje
                            emviasmesseg();
                            //apararece la animacion de carga
                            animcarga.setVisibility(View.VISIBLE);
                            anim.setBackgroundResource(R.drawable.animaciondecarga);
                            AnimationDrawable animacion = (AnimationDrawable) anim.getBackground();
                            animacion.start();
                            //EnviarMegeg("quiero alquilar una casa pero no puedo","Andrea mercedes araujo","985796307","arturo14212000@gmail.com");
                        }
                    });
                }else{
                    img.setImageResource(R.drawable.ic_baseline_mic_none_24);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Entradadevoz();
                        }
                    });
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Inicializar la libreria de voley para consulyas api res
        rq = Volley.newRequestQueue(this);

        contenMesseg.smoothScrollToPosition(View.SCROLL_INDICATOR_END);

        messeg = new ArrayList<>();
        messeg.add(new Messeg("Hola me llamo Sofia, y sere tu asistente en estos momentos",'r',horsnow(),null));
        AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
        contenMesseg.setAdapter(ad);

        animation_vos("Hola me llamo Sofia, y sere tu asistente en estos momentos");
    }

    //compartir memoria
    public void optimizar(){
        //con esto se limpiara la memoria y liberara un porcentaje de ella
        //evitando que la aplicacion se ahogue
        if(messeg.size() >= 8){
            ArrayList<Messeg> auxme = new ArrayList<>();
            for (int a = (messeg.size()-1)-7 ; a <= (messeg.size()-1) ; a++){
                auxme.add(messeg.get(a));
            }
            messeg.clear();
            for (Messeg e: auxme){
                messeg.add(e);
            }
        }
    }

    public void Entradadevoz(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Di lo que quieras que se envie");
        try {
            startActivityForResult(i,REQ_CODE);
        }catch (ActivityNotFoundException e){

        }
    }

    public void emviasmesseg(){
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
                    optimizar();
                    AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
                    contenMesseg.setAdapter(ad);
                    contenMesseg.smoothScrollToPosition(View.SCROLL_INDICATOR_END); //vajar el scroll
                    //Log.d("Messeg;","leng:"+ String.valueOf(obj.getString("messeg")).length()+" text: "+obj.getString("messeg"));
                    animation_vos(obj.getString("messeg"));
                    //en caso que las uno de los mensajes sea igual que el parametro que activa el form, se muestra el formulario
                    if (obj.getString("messeg").equals("Rellena el siguiente formulario")){
                        alertDialeg();
                    }
                    animcarga.setVisibility(View.GONE);
                    //optimizar();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ms","error");
                System.out.println("error" + error.toString());
                NetworkResponse networkResponse = error.networkResponse;

                if(networkResponse != null){
                    System.out.println("Error Response code: " + networkResponse.toString());
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> cabeceras = new HashMap<String, String>();
                cabeceras.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1)");
                return cabeceras;
            }
        };
        st.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
            System.out.println("resultadodimgError : "+"A susedio un error al tratar de inprimir");
        }
        return imgs;
    }

    public void animation_vos(String messeg){

        char[] lts = messeg.toCharArray();
        Integer[] listIm = {R.drawable.animationsinbrazos,R.drawable.animationbrazo1,R.drawable.animationbrazo};

        redimentImageView(0);

        //lanzar animacion
        try {
            iaperfil.setBackgroundResource(R.drawable.animacionhablar);
            AnimationDrawable ad = (AnimationDrawable) iaperfil.getBackground();
            ad.start();
        }catch (OutOfMemoryError error){
            System.out.println("Paralisis");
        }


        tts.speekOut(messeg);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ad.stop();
                redimentImageView(0);
                iaperfil.setBackgroundResource(R.drawable.animacionparpadeo);
                AnimationDrawable ad = (AnimationDrawable) iaperfil.getBackground();
                ad.start();
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
        getMenuInflater().inflate(R.menu.mymenumesseg,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salir:
                    Intent intent2 = new Intent(getApplicationContext(), login.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("EXIT", true);
                    startActivity(intent2);
                return true;
                case R.id.csecion:
                    auth.signOut();
                    //Cerrar sesión con google tambien: Google sign out
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Abrir MainActivity con SigIn button
                            if(task.isSuccessful()){
                                Intent mainActivity = new Intent(getApplicationContext(), login.class);
                                startActivity(mainActivity);
                            }else{
                                Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQ_CODE == resultCode && null != data){
            //resultado de lo hablado y enviado como mensaje
            ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            messeg.add(new Messeg(res.get(0),'e',horsnow(),null));
            AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
            contenMesseg.setAdapter(ad);
            ResponsedIA(edmes.getText().toString());
            edmes.setText("");
        }
    }

    public void alertDialeg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater ly = getLayoutInflater();
        View v = ly.inflate(R.layout.formularioinfo,null);
        Button b = v.findViewById(R.id.buttonenviarinfo);
        EditText messg = v.findViewById(R.id.formmessag);
        EditText nom = v.findViewById(R.id.formnombre);
        EditText num = v.findViewById(R.id.formtelefono);
        EditText corre = v.findViewById(R.id.formemail);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMegeg(messg.getText().toString(),nom.getText().toString(),num.getText().toString(),corre.getText().toString());
            }
        });
        builder.setView(v);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    public void EnviarMegeg(String msseg, String nomb, String num, String corr){
        rq = Volley.newRequestQueue(this);
        String parat = "?messeg="+msseg+"&nombre="+nomb+"&numero="+num+"&correo="+ corr;
        String url ="https://chatterbotte.herokuapp.com/app/correo";
        // Request a string response from the provided URL.
        StringRequest st=new StringRequest(
                Request.Method.GET,
                url + parat
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    messeg.add(new Messeg(obj.getString("mensaje"),'r',horsnow(),null));
                    AdapterUser ad=new AdapterUser(messeg,getApplicationContext(),getResources(),getLayoutInflater());
                    contenMesseg.setAdapter(ad);
                    contenMesseg.smoothScrollToPosition(View.SCROLL_INDICATOR_END);
                    animation_vos(obj.getString("mensaje"));
                    alert.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("ErrorVoley",error.getMessage());
            }
        });
        rq.add(st);
    }
}