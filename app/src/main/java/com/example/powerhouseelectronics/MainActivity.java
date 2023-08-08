package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity implements CustomAlert.OnDialogCloseListener {

    private String role;
    private CustomAlert.OnDialogCloseListener dialogCloseListener = new CustomAlert.OnDialogCloseListener() {
        @Override
        public void onDialogClose() {

            navigateToNextActivity(role);
        }
    };

    @Override
    public void onDialogClose() {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        if ("user".equals(role)) {
            Intent intent = new Intent(MainActivity.this, Index.class);
            startActivity(intent);
        } else if ("admin".equals(role) || "superadmin".equals(role)) {
            Intent intent = new Intent(MainActivity.this, PanelAdmin.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText SendEmail = findViewById(R.id.txtEmailLog);
        EditText SendPassword = findViewById(R.id.txtPassLog);
        Button btnRedireccionRegistro = findViewById(R.id.btnRegistro);
        Button btnIniciarSesion = findViewById(R.id.btnLogin);

        btnRedireccionRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void Login(String email, String password) {
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
                runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(MainActivity.this, "Error", "Error al hacer la solicitud: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Log.d("ServerResponse", "Response JSON: " + responseData);
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            String role = jsonObject.getJSONObject("user").getString("role");

                            SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", jsonObject.getString("token"));
                            editor.putString("id", jsonObject.getJSONObject("user").getString("_id"));

                            JSONObject userObject = jsonObject.getJSONObject("user");
                            editor.putString("name", userObject.getString("name"));
                            editor.putString("email", userObject.getString("email"));
                            editor.putString("image", userObject.getString("image"));
                            editor.putString("address", userObject.getString("address"));
                            editor.putString("phone", userObject.getString("phone"));
                            editor.putString("role", role);

                            editor.apply();

                            runOnUiThread(() -> {
                                CustomAlert.showCustomSuccessDialog(MainActivity.this, "¡Inicio de sesión exitoso!", "Has iniciado sesión correctamente", new CustomAlert.OnDialogCloseListener() {
                                    @Override
                                    public void onDialogClose() {
                                        navigateToNextActivity(role);
                                    }
                                });
                            });

                        } catch (JSONException e) {
                            runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(MainActivity.this, "Error", "Error al analizar la respuesta JSON: " + e.getMessage()));
                        }
                    } else {
                        if (response.code() == 401 || response.code() == 403) {
                            EliminarToken();
                            runOnUiThread(() -> {
                                CustomErrorAlert.showCustomErrorDialog(MainActivity.this, "Error", "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.");
                                logout();
                            });
                        } else {
                            runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(MainActivity.this, "Error", "Email o contraseña incorrectos. Intenta nuevamente!! "));
                        }
                    }
                } catch (IOException e) {
                    runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(MainActivity.this, "Error", "Error al obtener la respuesta: " + e.getMessage()));
                }
            }
        });
    }

    private void showErrorAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void EliminarToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.clear();
        editor.apply();
    }

    private void logout() {
        EliminarToken();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToNextActivity(String role) {
        if (role.equals("user")) {
            Intent intent = new Intent(MainActivity.this, Index.class);
            startActivity(intent);
        } else if (role.equals("admin") || role.equals("superadmin")) {
            Intent intent = new Intent(MainActivity.this, PanelAdmin.class);
            startActivity(intent);
        }
    }
}