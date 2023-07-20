package com.example.powerhouseelectronics;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegister extends AppCompatActivity {


    UsersClass user;
    Button btnRegistrar;

    EditText txtEmail, txtPass, txtNombre, txtDireccion, txtNum;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

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

        return new UsersClass(name, email, password, address, phone);
    }


    private void enviarDatosUsuario(UsersClass user) {
        Api Api = ApiUrl.getRetrofitInstance().create(Api.class);


        Call<Void> call = Api.registerUser(
                RequestBody.create(MediaType.parse("text/plain"), user.getName()),
                RequestBody.create(MediaType.parse("text/plain"), user.getEmail()),
                RequestBody.create(MediaType.parse("text/plain"), user.getPassword()),
                RequestBody.create(MediaType.parse("text/plain"), user.getAddress()),
                RequestBody.create(MediaType.parse("text/plain"), user.getPhone())
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