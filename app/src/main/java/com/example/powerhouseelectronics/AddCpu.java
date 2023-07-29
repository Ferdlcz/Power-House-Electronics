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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class AddCpu extends AppCompatActivity {

    Toolbar toolbar;

    EditText editTextMarca, editTextModelo, editTextProcesador, editTextRam, editTextStorage, editTextPrecio, editTextSistemaOperativo, editTextTarjetaGrafica, editTextStock;

    ImageView DefaultImage2;

    Uri selectedImageUri;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_add_cpu);

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

        editTextMarca = (EditText) findViewById(R.id.editTextBrand);
        editTextModelo = (EditText) findViewById(R.id.editTextModel);
        editTextPrecio = (EditText) findViewById(R.id.editTextPrice);
        editTextStorage = (EditText) findViewById(R.id.editTextStorage);
        editTextTarjetaGrafica = (EditText) findViewById(R.id.editTextGraphicsCard);
        editTextProcesador = (EditText) findViewById(R.id.editTextProcessor);
        editTextRam = (EditText) findViewById(R.id.editTextRam);
        editTextSistemaOperativo = (EditText) findViewById(R.id.editTextOperatingSystem);
        editTextStock = (EditText) findViewById(R.id.editTextStock);


        DefaultImage2 = (ImageView) findViewById(R.id.DefaultImage2);
        Button BtnAddCpu = findViewById(R.id.btnRegisterCpu);

        BtnAddCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CpuClass products = obtenerProductosForm();
                EnviarProductos(products);
            }
        });


    }
    private CpuClass obtenerProductosForm(){
        String marca = editTextMarca.getText().toString();
        String modelo = editTextModelo.getText().toString();
        String precio = editTextPrecio.getText().toString();
        String almacenamiento = editTextStorage.getText().toString();
        String sistemaOperativo = editTextSistemaOperativo.getText().toString();
        String ram = editTextRam.getText().toString();
        String procesador = editTextProcesador.getText().toString();
        String tarjetaGrfica = editTextTarjetaGrafica.getText().toString();
        String stock = editTextStock.getText().toString();
        String imagePath = obtenerRutaImg();

        return new CpuClass(marca, modelo, precio, almacenamiento, sistemaOperativo, procesador, tarjetaGrfica, ram, stock, imagePath);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
            DefaultImage2.setImageURI(path);
            if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                String imagePath = obtenerRutaImg();
                DefaultImage2.setImageURI(selectedImageUri);
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
    private void EnviarProductos(CpuClass products) {
        Api api = ApiUrl.getRetrofitInstance().create(Api.class);

        File imageFile = new File(products.getImage());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);

        Log.d("API_CALL_REQUEST", "Image Name: " + imageFile.getName());
        Log.d("API_CALL_REQUEST", "Image File Path: " + imageFile.getAbsolutePath());

        Call<Void> call = api.registerCpu(
                RequestBody.create(MediaType.parse("text/plain"), products.getBrand()),
                RequestBody.create(MediaType.parse("text/plain"), products.getModel()),
                RequestBody.create(MediaType.parse("text/plain"), products.getPrice()),
                RequestBody.create(MediaType.parse("text/plain"), products.getRam()),
                RequestBody.create(MediaType.parse("text/plain"), products.getStorage()),
                RequestBody.create(MediaType.parse("text/plain"), products.getProcessor()),
                RequestBody.create(MediaType.parse("text/plain"), products.getGraphicsCard()),
                RequestBody.create(MediaType.parse("text/plain"), products.getOperatingSystem()),
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
        Intent intent = new Intent(AddCpu.this, Profile.class);
        startActivity(intent);
    }

}

