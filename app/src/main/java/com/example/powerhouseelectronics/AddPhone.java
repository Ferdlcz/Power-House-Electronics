package com.example.powerhouseelectronics;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPhone extends AppCompatActivity {

    Toolbar toolbar;

    EditText editTextMarca, editTextModelo, editTextColor, editTextAlmacenamiento, editTextPrecio, editTextResolucion, editTextCamara, editTextStock;

    ImageView DefaultImage;

    Uri selectedImageUri;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

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

        editTextMarca = (EditText) findViewById(R.id.txtMarca);
        editTextModelo = (EditText) findViewById(R.id.txtModelo);
        editTextColor = (EditText) findViewById(R.id.txtCaracteristicas);
        editTextAlmacenamiento = (EditText) findViewById(R.id.txtAlmacenamiento);
        editTextPrecio = (EditText) findViewById(R.id.txtPrecio);
        editTextResolucion = (EditText) findViewById(R.id.txtColor);
        editTextCamara = (EditText) findViewById(R.id.txtCantidad);
        editTextStock = (EditText) findViewById(R.id.TextStock);

        DefaultImage = (ImageView) findViewById(R.id.DefaultImage3);
        Button BtnAddPhone = findViewById(R.id.btnRegisterConsole);

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
        String stock = editTextStock.getText().toString();
        String imagePath = obtenerRutaImg();

        return new PhoneClass(marca, modelo, color, almacenamiento , precio, resolucion, camara, stock,imagePath);
    }

    private String obtenerRutaImg() {
        if (selectedImageUri == null) {
            return null;
        }
        return getPathFromUri(selectedImageUri);
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona la aplicación"), 10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona la aplicación"), 10);
            } else {
                Toast.makeText(this, "Permiso de lectura del almacenamiento externo denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            DefaultImage.setImageURI(path);
            if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                String imagePath = obtenerRutaImg();
                DefaultImage.setImageURI(selectedImageUri);
            }
        }
    }
        private String getPathFromUri(Uri uri) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            }
            return null;
        }

    private void EnviarProductos(PhoneClass products) {
        Api api = ApiUrl.getRetrofitInstance().create(Api.class);

        File imageFile = new File(products.getImage());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);

        Log.d("API_CALL_REQUEST", "Image Name: " + imageFile.getName());
        Log.d("API_CALL_REQUEST", "Image File Path: " + imageFile.getAbsolutePath());

        Call<Void> call = api.registerPhone(
                RequestBody.create(MediaType.parse("text/plain"), products.getBrand()),
                RequestBody.create(MediaType.parse("text/plain"), products.getModel()),
                RequestBody.create(MediaType.parse("text/plain"), products.getColor()),
                RequestBody.create(MediaType.parse("text/plain"), products.getStorage()),
                RequestBody.create(MediaType.parse("text/plain"), products.getPrice()),
                RequestBody.create(MediaType.parse("text/plain"), products.getScreenResolution()),
                RequestBody.create(MediaType.parse("text/plain"), products.getCameraResolution()),
                RequestBody.create(MediaType.parse("text/plain"), products.getStock()),
                imagePart
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
                        Log.e("API_CALL_ERROR", errorResponse);
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


