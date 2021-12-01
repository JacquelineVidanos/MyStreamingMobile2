package com.example.mystreamingmobile;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText etUsuario;
    private EditText etPass;
    private String Correo;
    private String Password;
    private boolean respuesta = false;
    //Conexión remota
    private RequestQueue conexionServidor;
    private StringRequest peticionServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enlace a las vistas
        etUsuario = findViewById(R.id.et_usuario);
        etPass = findViewById(R.id.et_pass);

        // variable conexión a servidor
        conexionServidor = Volley.newRequestQueue(this);
    }//termina onCreate
    public void loginBtn (final View v) {
        v.setEnabled(true);
        Correo = etUsuario.getText().toString();
        Password = etPass.getText().toString();


        peticionServidor = new StringRequest(
                Request.Method.GET,
                "http://10.0.2.2:5000/api/Usuarios/LoginAdm?usu=" + Correo+"&pass="+Password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        v.setEnabled(false);
                        //if (response.equals("ERROR")) {
                        if (!response.contains(Password)) {
                            v.setEnabled(true);
                            Toast.makeText(MainActivity.this, "Usuario o contraseña incorecto", Toast.LENGTH_SHORT).show();
                        } else {
                            v.setEnabled(true);
                            //Toast.makeText(LoginActivity.this, "Bienvenido " + etUsuario.getText().toString(), Toast.LENGTH_SHORT).show(); // doble toast? el 'otro' toast viene de code igniter
                            final String nombre = etUsuario.getText().toString();
                            startActivity(new Intent(MainActivity.this, PrincipalActivity.class)//.putExtra("nombre", nombre) // estoy usando un putExtra de más?
                                    .putExtra("nombre", "Raúl")
                            ); // doble toast? quitar uno
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show(); // Este msg devuelve el error de volley
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usu", Correo);
                parametros.put("pass", Password);

                return parametros;
            }
        };

        conexionServidor.add(peticionServidor);
    }

}