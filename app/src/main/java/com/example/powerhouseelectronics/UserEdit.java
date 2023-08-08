package com.example.powerhouseelectronics;
//usuarios normales

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
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class UserEdit extends AppCompatActivity implements CustomAlert.OnDialogCloseListener{

    private void navigateToMainActivity() {
        Intent intent = new Intent(UserEdit.this, Index.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onDialogClose() {
        navigateToMainActivity();
    }

    Toolbar toolbar;
    String modifierId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_user_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences info = getSharedPreferences("Token", MODE_PRIVATE);
        String userName = info.getString("name", "Nombre de usuario");
        TextView userNameTextView = findViewById(R.id.user_name);
        userNameTextView.setText(userName);

        CircleImageView userProfileImageView = findViewById(R.id.user_profile_image);

        String userImageURL = info.getString("image", "");

        Picasso.with(this)
                .load(userImageURL)
                .into(userProfileImageView);


        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        modifierId = sharedPreferences.getString("id", "");

        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String image = sharedPreferences.getString("image", "");
        String address = sharedPreferences.getString("address", "");
        String phone = sharedPreferences.getString("phone", "");

        EditText nameEditText = findViewById(R.id.editTextName);
        nameEditText.setText(name);

        EditText emailEditText = findViewById(R.id.editTextEmail);
        emailEditText.setText(email);

        EditText phoneEditText = findViewById(R.id.editTextPhone);
        phoneEditText.setText(phone);

        EditText addressEditText = findViewById(R.id.editTextAddress);
        addressEditText.setText(address);

        ImageView imageView = findViewById(R.id.imageView4);
        Picasso.with(this)
                .load(image)
                .into(imageView);

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


        String jsonBody = "{"
                + "\"name\":\"" + newName + "\","
                + "\"email\":\"" + newEmail + "\","
                + "\"phone\":\"" + newPhone + "\","
                + "\"address\":\"" + newAddress + "\","
                + "\"modifierId\":\"" + modifierId + "\""
                + "}";

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/users/" + modifierId)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    runOnUiThread(() -> {
                        CustomAlert.showCustomSuccessDialog(UserEdit.this, "¡Actualizacion exitosa!", "Usuario actualizado exitosamente", UserEdit.this);
                    });
                    //Log.d("USER_UPDATE", "Actualización exitosa");

                    SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", newName);
                    editor.putString("email", newEmail);
                    editor.putString("phone", newPhone);
                    editor.putString("address", newAddress);
                    editor.apply();

                    finish();
                } else {
                    runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(UserEdit.this, "Error", "Se ha producido un error al actualizar usuario: " + response.code() ));
                    //Log.d("USER_UPDATE", "Error al actualizar: " + response.code());
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
        }else if (item.getItemId() == R.id.profile){
            GoProfile();
            return false;
        } else if (item.getItemId() == R.id.carrito){
            GoCarrito();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        SharedPreferences CartItem = getSharedPreferences("CartItems", MODE_PRIVATE);
        SharedPreferences.Editor edit = CartItem.edit();
        edit.remove("CartItemsList");
        editor.apply();
        edit.apply();
    }

    private void logout() {
        removeTokenFromSharedPreferences();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void GoProfile() {
        Intent intent = new Intent(UserEdit.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(UserEdit.this, Carrito.class);
        startActivity(intent);
    }
}