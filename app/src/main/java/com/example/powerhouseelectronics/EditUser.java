package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditUser extends AppCompatActivity {

    Toolbar toolbar;
    String modifierId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewUsers.UserResponse user = (ViewUsers.UserResponse) getIntent().getSerializableExtra("user_data");

        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        modifierId = sharedPreferences.getString("id", "");
        userId = getIntent().getStringExtra(ViewUsers.EXTRA_USER_ID);

        if (user != null) {
            // Cargamos los datos del usuario en la interfaz gráfica
            ImageView imageView = findViewById(R.id.imageView4);
            Picasso.with(this)
                    .load(user.getImage())
                    .into(imageView);

            EditText nameEditText = findViewById(R.id.editTextName);
            nameEditText.setText(user.getName());

            EditText emailEditText = findViewById(R.id.editTextEmail);
            emailEditText.setText(user.getEmail());

            EditText phoneEditText = findViewById(R.id.editTextPhone);
            phoneEditText.setText(user.getPhone());

            EditText addressEditText = findViewById(R.id.editTextAddress);
            addressEditText.setText(user.getAddress());

            EditText roleEditText = findViewById(R.id.editTextRole);
            roleEditText.setText(user.getRole());
        }

        Button btnUpdateUser = findViewById(R.id.btnUpdateUser);
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();
            }
        });
    }

    private void updateUserInformation() {
        EditText nameEditText = findViewById(R.id.editTextName);
        String newName = nameEditText.getText().toString();

        EditText emailEditText = findViewById(R.id.editTextEmail);
        String newEmail = emailEditText.getText().toString();

        EditText phoneEditText = findViewById(R.id.editTextPhone);
        String newPhone = phoneEditText.getText().toString();

        EditText addressEditText = findViewById(R.id.editTextAddress);
        String newAddress = addressEditText.getText().toString();

        EditText roleEditText = findViewById(R.id.editTextRole);
        String newRole = roleEditText.getText().toString();

        String jsonBody = "{"
                + "\"name\":\"" + newName + "\","
                + "\"email\":\"" + newEmail + "\","
                + "\"phone\":\"" + newPhone + "\","
                + "\"address\":\"" + newAddress + "\","
                + "\"role\":\"" + newRole + "\","
                + "\"modifierId\":\"" + modifierId + "\""
                + "}";

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/users/" + userId)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("USER_UPDATE", "Actualización exitosa");
                    finish(); // Cerramos la actividad actual (EditUser) y volvemos a ViewUsers
                } else {
                    Log.d("USER_UPDATE", "Error al actualizar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("USER_UPDATE", "Error en la solicitud: " + e.getMessage());
            }
        });

        Log.d("USER_UPDATE", "JSON Request Body: " + jsonBody);
        Log.d("USER_UPDATE", "URL: " + request.url());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            logout();
            return false;
        } else if (item.getItemId() == R.id.profile) {
            GoProfile();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();
    }

    private void logout() {
        removeTokenFromSharedPreferences();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void GoProfile() {
        Intent intent = new Intent(EditUser.this, Profile.class);
        startActivity(intent);
    }
}