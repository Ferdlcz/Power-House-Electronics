package com.example.powerhouseelectronics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPhone extends AppCompatActivity {

    Toolbar toolbar;

    EditText editTextMarca, editTextModelo, editTextColor, editTextAlmacenamiento, editTextPrecio, editTextResolucion, editTextCamara, editTextImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_add_phone);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", "Nombre de usuario");
        TextView userNameTextView = findViewById(R.id.user_name);
        userNameTextView.setText(userName);

        CircleImageView userProfileImageView = findViewById(R.id.user_profile_image);

        String userImageURL = sharedPreferences.getString("image", "");

        Picasso.with(this)
                .load(userImageURL)
                .into(userProfileImageView);

        editTextMarca = (EditText) findViewById(R.id.editTextMarca);
        editTextModelo = (EditText) findViewById(R.id.editTextModelo);
        editTextColor = (EditText) findViewById(R.id.editTextColor);
        editTextAlmacenamiento = (EditText) findViewById(R.id.editTextAlmacenamiento);
        editTextPrecio = (EditText) findViewById(R.id.editTextPrecio);
        editTextResolucion = (EditText) findViewById(R.id.editTextResolucion);
        editTextCamara = (EditText) findViewById(R.id.editTextCamara);
        editTextImagen = (EditText) findViewById(R.id.editTextImagen);

        Button BtnAddPhone = findViewById(R.id.btnRegisterPhone);

        BtnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneClass products = obtenerProductosForm();
                EnviarProductos(products);
            }
        });
    }


    private PhoneClass obtenerProductosForm(){

        String marca = editTextMarca.getText().toString();
        String modelo = editTextModelo.getText().toString();
        String color = editTextColor.getText().toString();
        String almacenamiento = editTextAlmacenamiento.getText().toString();
        String precio = editTextPrecio.getText().toString();
        String resolucion = editTextResolucion.getText().toString();
        String camara = editTextCamara.getText().toString();
        String imagen = editTextImagen.getText().toString();

        return new PhoneClass(marca, modelo, color, almacenamiento , precio, resolucion, camara, imagen);
    }

    private void EnviarProductos(PhoneClass products){
        Api Api = ApiUrl.getRetrofitInstance().create(Api.class);

        Call<Void> call = Api.registerPhone(
                RequestBody.create(MediaType.parse("text/plain"), products.getBrand()),
                RequestBody.create(MediaType.parse("text/plain"), products.getModel()),
                RequestBody.create(MediaType.parse("text/plain"), products.getColor()),
                RequestBody.create(MediaType.parse("text/plain"), products.getStorage()),
                RequestBody.create(MediaType.parse("text/plain"), products.getPrice()),
                RequestBody.create(MediaType.parse("text/plain"), products.getScreenResolution()),
                RequestBody.create(MediaType.parse("text/plain"), products.getCameraResolution()),
                RequestBody.create(MediaType.parse("text/plain"), products.getImage())
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        Toast.makeText(getApplicationContext(), "Error en el registro: " + errorResponse, Toast.LENGTH_SHORT).show();
                        Log.e("API_CALL_ERROR", errorResponse );
                        Log.d("API_CALL_URL", call.request().url().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMessage = "Error al realizar la llamada: " + t.getMessage();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("API_CALL_ERROR", errorMessage);
            }
        });
    }
}