package com.example.powerhouseelectronics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Profile extends AppCompatActivity {

    Toolbar toolbar;
    Button btnDeleteAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnRedireccionEdit = findViewById(R.id.btnEditUser);

        btnRedireccionEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, UserEdit.class);
                startActivity(intent);
            }
        });

        btnDeleteAcc = findViewById(R.id.btnDeleteAcc);
        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertConfirm();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String image = sharedPreferences.getString("image", "");
        String address = sharedPreferences.getString("address", "");
        String phone = sharedPreferences.getString("phone", "");

        TextView txtName = findViewById(R.id.TxtName);
        TextView txtEmail = findViewById(R.id.txtGmail);
        TextView txtAddress = findViewById(R.id.txtAdress);
        TextView txtPhone = findViewById(R.id.txtPhone);

        txtName.setText(name);
        txtEmail.setText(email);
        txtAddress.setText(address);
        txtPhone.setText(phone);

        CircleImageView imageView = findViewById(R.id.imageView3);
        Picasso.with(this)
                  .load(image)
                  .into(imageView);
    }

    private void AlertConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar cuenta");
        builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario confirma la eliminación, realiza la solicitud al servidor
                // para eliminar la cuenta y cierra la sesión del usuario
                deleteUserAccount();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void deleteUserAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String modifierId = sharedPreferences.getString("id", "");

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String jsonBody = "{\"modifierId\":\"" + modifierId + "\"}";

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/users/" + modifierId)
                .delete(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("USER_DELETE", "Usuario eliminado correctamente");
                } else {
                    Log.d("USER_DELETE", "Error al eliminar usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("USER_DELETE", "Error en la solicitud: " + e.getMessage());
            }
        });

        logout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");
        String address = sharedPreferences.getString("address", "");

        TextView nameTextView = findViewById(R.id.TxtName);
        TextView emailTextView = findViewById(R.id.txtGmail);
        TextView phoneTextView = findViewById(R.id.txtPhone);
        TextView addressTextView = findViewById(R.id.txtAdress);

        nameTextView.setText(name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
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
            }else if (item.getItemId() == R.id.profile){
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

        private void GoProfile (){
            Intent intent = new Intent(Profile.this, Profile.class);
            startActivity(intent);
        }

}