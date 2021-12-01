package com.example.mystreamingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class AddAlertaActivity extends AppCompatActivity {
    public EditText etAddAlert;

    private String AddAlert;
    private boolean respuesta = false;
    //Conexión remota
    private RequestQueue conexionServidor;
    private StringRequest peticionServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alerta);
        // Enlace a las vistas
        etAddAlert = findViewById(R.id.et_text);

        // variable conexión a servidor
        conexionServidor = Volley.newRequestQueue(this);
    }//termina onCreate

    public void volleyPost(View view){
        AddAlert = etAddAlert.getText().toString();
        final int a = Integer.parseInt(AddAlert);
        String postUrl = "http://10.0.2.2:5000/api/Alertas/AddAlerta";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("meta_suscriptores", a);
            jsonParams.put("estatus", true);
            jsonParams.put("num_suscriptores", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Toast.makeText(AddAlertaActivity.this, "Hecho", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddAlertaActivity.this, AlertasActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(new Intent(AddAlertaActivity.this, AlertasActivity.class));
                //Toast.makeText(AddAlertaActivity.this, error.getStackTrace().toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}