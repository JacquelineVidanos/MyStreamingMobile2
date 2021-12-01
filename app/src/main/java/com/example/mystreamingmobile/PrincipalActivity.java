package com.example.mystreamingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class PrincipalActivity extends AppCompatActivity {
    private String nombre;

    private TextView tvJugador;
    private TextView tvNum;
    private RequestQueue conexionServidor;
    private StringRequest peticionServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        nombre = getIntent().getStringExtra("nombre"); // Se obtiene el nombre del jugador, debe contener el mismo texto que el de .putExtra de vista anterior
        tvJugador = findViewById(R.id.textView); // se inicializa la vista
        tvNum = findViewById((R.id.textView3));
        tvJugador.setText("Bienvenido " + nombre); // se inserta el nombre del jugador en la vista
        consultaAlertas();

    }//termina onCreate

    public void consultaAlertas() {
        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url ="http://10.0.2.2:5000/api/Suscripciones/GetNumSus";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvNum.setText(""+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }
    public void Siguiente(View view) {
        startActivity(new Intent(PrincipalActivity.this,AlertasActivity.class));
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerta;
        alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Cerrar sesión");
        alerta.setMessage("¿Realmente desea salir?");
        alerta.setCancelable(false);
        alerta.setIcon(android.R.drawable.ic_dialog_info);

        alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // termina actividar y se debe reiniciar
                        startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
                    }
                }
        );
        alerta.setNeutralButton(
                "No", null
        );
        alerta.show();
    }
}