package com.example.powerhouseelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText SendEmail = findViewById(R.id.txtEmailLog);
        EditText SendPassword = findViewById(R.id.txtPassLog);
        Button btnRedireccionRegistro = findViewById(R.id.btnRegistro);
        Button btnIniciarSesion = findViewById(R.id.btnLogin);
        btnRedireccionRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, UserRegister.class);
                startActivity(intent);
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = SendEmail.getText().toString();
                String password = SendPassword.getText().toString();
                Login(email, password);
            }
        });
    }
        private void Login(String email, String password){
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url("http://173.255.204.68/api/users/login")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("LoginError", "Error al hacer la solicitud: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                String role = jsonObject.getJSONObject("user").getString("role");

                                if (role.equals("user")) {
                                    Intent intent = new Intent(MainActivity.this, Index.class);
                                    startActivity(intent);
                                } else if (role.equals("admin") || role.equals("superadmin")) {
                                    Intent intent = new Intent(MainActivity.this, PanelAdmin.class);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                Log.e("LoginError", "Error al analizar la respuesta JSON: " + e.getMessage());
                            }
                        } else {
                            Log.e("LoginError", "Error en la respuesta del servidor: " + response.message());
                        }
                    } catch (IOException e) {
                        Log.e("LoginError", "Error al obtener la respuesta: " + e.getMessage());
                    }
                }
            });
        }
}