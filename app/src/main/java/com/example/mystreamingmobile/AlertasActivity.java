package com.example.mystreamingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlertasActivity extends AppCompatActivity {
    private SwipeRefreshLayout srlRanking;
    private ListView lvRanking;
    private ArrayAdapter<String> adaptador;
    private ArrayList<String> listarRanking;

    private RequestQueue conexionServidor;
    private StringRequest peticionServidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);
        srlRanking = findViewById(R.id.srl_ranking);
        lvRanking = findViewById(R.id.lv_ranking);
        listarRanking = new ArrayList<>();

        // ligar contenido con el arrayadapter
        adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, // la vista no la genero yo la genera android
                listarRanking
        );

        lvRanking.setAdapter(adaptador);

        conexionServidor = Volley.newRequestQueue(this);

        // se ejecuta al inicio, invocar al swipe y mostrar el contenido
        srlRanking.post( //programar un hilo (post significa despues de cargar...)
                new Runnable() {
                    @Override
                    public void run() {
                        srlRanking.setRefreshing(true); // actualiza swipe
                        consultaAlertas();  // carga el metodo
                    }
                }
        );

        //event swipe
        srlRanking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { // en teoria hay diferencia... entre este y el anterior
                srlRanking.setRefreshing(true);
                consultaAlertas();
            }
        });
    }//termina onCreate
    // Metodo que consume el servicio qeu retorna la lsita de Ranking
    public void consultaAlertas() {
        peticionServidor = new StringRequest( // esto vendra en el examen
                /*
                1. Tipo de petición
                2. Url donde esta el servicio
                3. ación si el servicio funciona
                4. accion si se genra un error
                5. si es post, pasar variables
                 */
                Request.Method.GET,
                "http://10.0.2.2:5000/api/Alertas/GetAlertas", // crear servicio...
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // intentar convertir response en objeto json
                        try {
                            JSONArray objResp = new JSONArray(response);
                            //JSONArray objResp = response.("paises");
// Este solo se usa si quiero llamar otro objeto del json -------- el toast devuelve pues un toast con la info del json {[{json}]}
//                            Toast.makeText(RankingActivity.this,
//                                    objResp.getString("mensaje"), // devuelve "mensaje" del json que fue declarado en el controlador ej. mensaje: hola
//                                    Toast.LENGTH_LONG).show();

                            // Tomar el arreglo de la posición "lista_score"
                            //JSONObject obtenRanking = objResp.getJSONObject(0); // lista_score es el nombre el objteto en el json (ej ranking [{1, paco, 220}, {2, luis, 330}]

                            // eliminar el contenido anterior para evitar duplicas del mismo
                            listarRanking.clear();


                            // hacer for paraa agregar los objetos en la lista del adaptador
                            for(int i = 0 ; i < objResp.length() ; i++) {
                                //creamos un objeto para cada elemento
                                JSONObject ranking2 = objResp.getJSONObject(i);
                                //JSONObject ranking2 = obtenRanking.getJSONObject(i);
                                // Agregamos los valores a la lista del adaptador
                                listarRanking.add(
                                        "Alerta "+ranking2.getInt("id") + ". Avisarme cuando llegue a: " + ranking2.getInt("meta_suscriptores") + " suscriptores. Número actual de suscriptores: " + ranking2.getInt ("num_suscriptores")+ "."); //getString, getInt
                            }

                            // actualizo el adaptador con el valor nuevo de la lista (actualización de la lista
                            adaptador.notifyDataSetChanged();
                        }
                        catch(Exception ex) {
                            Toast.makeText(
                                    AlertasActivity.this,
                                    "Error :( "+ex.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        // actualizo el adaptador con el valor nuevo de la lista si falla o si lo logra
                        srlRanking.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                AlertasActivity.this,
                                "Error --»"+error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                        // actualizo el adaptador con el valor nuevo de la lista si falla o si lo logra
                        srlRanking.setRefreshing(false);
                    }
                }

        );
        // como es un get se ejecuta el servicio
        conexionServidor.add(peticionServidor);

    }
    public void IrAddAlerta(View view) {
        startActivity(new Intent(AlertasActivity.this,AddAlertaActivity.class));
    }
}