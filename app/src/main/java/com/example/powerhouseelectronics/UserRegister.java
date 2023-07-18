package com.example.powerhouseelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegister extends AppCompatActivity {

    ImageView imagen;
    UsersClass user;
    Button btnRegistrar;
    Uri selectedImageUri;

    EditText txtEmail, txtPass, txtNombre, txtDireccion, txtNum;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        imagen = (ImageView) findViewById(R.id.UpImg);
        btnRegistrar = (Button) findViewById(R.id.BtnRegistrar);

         txtEmail = (EditText) findViewById(R.id.txtEmail);
         txtPass = (EditText) findViewById(R.id.txtPass);
         txtNombre = (EditText) findViewById(R.id.txtNombre);
         txtDireccion = (EditText) findViewById(R.id.txtDireccion);
         txtNum = (EditText) findViewById(R.id.txtNum);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersClass user = obtenerDatosDelFormulario();
                enviarDatosUsuario(user);
            }
        });
    }



    private UsersClass obtenerDatosDelFormulario() {

        String name = txtNombre.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPass.getText().toString();
        String address = txtDireccion.getText().toString();
        String phone = txtNum.getText().toString();
        String imagePath = obtenerRutaDeImagenSeleccionada();

        return new UsersClass(name, email, password, address, phone, imagePath);
    }

    private String obtenerRutaDeImagenSeleccionada() {
        if (selectedImageUri == null) {
            return null;
        }
        return getPathFromUri(selectedImageUri);
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona la aplicación"), 10);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Selecciona la aplicación"), 10);
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
        if(requestCode == 10 && resultCode== RESULT_OK && data != null){
            selectedImageUri = data.getData();
            String imagePath = obtenerRutaDeImagenSeleccionada();
            imagen.setImageURI(selectedImageUri);
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

    private void enviarDatosUsuario(UsersClass user) {
        Api Api = ApiUrl.getRetrofitInstance().create(Api.class);

        File imageFile = new File(user.getImage());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);

        Call<Void> call = Api.registerUser(
                RequestBody.create(MediaType.parse("text/plain"), user.getName()),
                RequestBody.create(MediaType.parse("text/plain"), user.getEmail()),
                RequestBody.create(MediaType.parse("text/plain"), user.getPassword()),
                RequestBody.create(MediaType.parse("text/plain"), user.getAddress()),
                RequestBody.create(MediaType.parse("text/plain"), user.getPhone()),
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