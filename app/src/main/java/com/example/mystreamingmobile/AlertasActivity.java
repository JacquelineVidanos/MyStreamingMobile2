package com.example.mystreamingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class AlertasActivity extends AppCompatActivity {
    private SwipeRefreshLayout srlRanking;
    private ListView lvRanking;
    private ArrayAdapter<String> adaptador;
    private ArrayList<String> listarRanking;
    private int nS;
    private int nM;

    private RequestQueue conexionServidor;
    private StringRequest peticionServidor;
    //creacion de variables para las notificaciones
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);
        srlRanking = findViewById(R.id.srl_ranking);
        lvRanking = findViewById(R.id.lv_ranking);
        listarRanking = new ArrayList<>();

        nS = getIntent().getIntExtra("numS",0);

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
                            listarRanking.clear();
                            for(int i = 0 ; i < objResp.length() ; i++) {
                                //creamos un objeto para cada elemento
                                JSONObject ranking2 = objResp.getJSONObject(i);
                                //JSONObject ranking2 = obtenRanking.getJSONObject(i);
                                // Agregamos los valores a la lista del adaptador
                                if(nS>=ranking2.getInt("meta_suscriptores")){
                                    //llamar al metodo de creacion de la notificacion
                                    createNotificationChannel();

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

                                    //se agrega la parte del reloj
                                    NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender().setHintHideIcon(true);

                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setContentTitle("¡Hey, Raúl!");
                                    builder.setContentText("Haz alcanzado los "+nS+" suscriptores, felicidades.");
                                    builder.setColor(Color.BLUE);
                                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                    builder.setLights(Color.MAGENTA, 1000, 1000);
                                    builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
                                    builder.setDefaults(Notification.DEFAULT_SOUND);
                                    //se vincula con el reloj
                                    builder.extend(wearableExtender);

                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());


                                    //PendingIntent pendingIntent = PendingIntent.getActivity(AlertasActivity.this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    //builder.setContentIntent(pendingIntent);

                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    nm.notify(NOTIFICACION_ID, builder.build());

                                }
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
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificación";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}