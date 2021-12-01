package com.example.mystreaming;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Context context;

    public static final String EXTRA_MESSAGE = "com.example.mystreaming.MESSAGE";

    private Button sendBtn;
    final EditText etUsuario = (EditText) findViewById(R.id.et_usuario);
    final EditText etPassword = (EditText) findViewById(R.id.et_pass);
    String usu = etUsuario.getText().toString();
    String pass = etPassword.getText().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = findViewById(R.id.btnLogin);
        sendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                if (etUsuario.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ambos campos son requeridos", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                IniciarSesion(usu, pass);
            }
        });
    }
    private void IniciarSesion(String usu, String pass){

        final TextView textView = (TextView) findViewById(R.id.text);
        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);

        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //String urlcom = "https://mystreaming.azurewebsites.net/api/usuarios/1"+1;
        String url ="http://localhost:64757/api/Usuarios/LoginUsuario";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            if(!response.isEmpty()){
                                Toast.makeText(MainActivity.this, "Hola de nuevo " + usu, Toast.LENGTH_SHORT).show();

                                EditText editText = (EditText) findViewById(R.id.et_usuario);
                                String message = editText.getText().toString();
                                intent.putExtra(EXTRA_MESSAGE, message);
                                startActivity(intent);
                            }else if(response.isEmpty()){
                                Toast.makeText(MainActivity.this, "Error: Uno de los datos no es correcto, vuelva a intentar", Toast.LENGTH_SHORT).show();
                            }

                           /* JSONObject respObj = new JSONObject(response);

                            String tipoUsuario = respObj.getString("tipo_usuario");
                            String usu = respObj.getString("correo");
                            String pwd = respObj.getString("contrasenia");
                            int tu = Integer.parseInt(tipoUsuario);
                            if(tu == 1){
                                if (usu == etUsuario.getText().toString() && pwd == etPassword.getText().toString()){
                                    Toast.makeText(MainActivity.this, "Hola de nuevo " + usu, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MainActivity.this, "Error: Uno de los datos no es correcto, vuelva a intentar", Toast.LENGTH_SHORT).show();
                                }*/


                            //textView.setText("Hola de nuevo: "+ name);
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "ERROR --> " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "ERROR --> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("usu", usu);
                params.put("pass", pass);

                // at last we are
                // returning our params.
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        //Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/todos/1").build();

       /* peticionServidor = new StringRequest(
                Request.Method.GET,
                "https://mystreaming.azurewebsites.net/api/usuarios/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respObj = new JSONObject(response);

                            String name = respObj.getString("correo");

                            textView.setText("Hola de nuevo: "+ response.toString());
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "ERROR --> " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "ERROR --> " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //Evitamos la ejecución doble
        peticionServidor.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        //Ejecutar el servicio
        conexionServidor.add(peticionServidor);*/
    }
}