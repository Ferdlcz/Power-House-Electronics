package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class EditConsoles extends AppCompatActivity implements CustomAlert.OnDialogCloseListener{

    private void navigateToMainActivity() {
        Intent intent = new Intent(EditConsoles.this, ViewProducts.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onDialogClose() {
        navigateToMainActivity();
    }

    Toolbar toolbar;

    LinearLayout productLayout;

    EditText txtEditPrice, txtEditStock;

    String productId, initialPrice, initialStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_edit_consoles);
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

        productLayout = findViewById(R.id.product_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getString("productId");
            initialPrice = extras.getString("price");
            initialStock = extras.getString("stock");
        }

        txtEditPrice = findViewById(R.id.txtEditConsolePrice);
        txtEditStock = findViewById(R.id.txtEditConsoleStock);

        Button btnEditProduct = findViewById(R.id.btnEditConsole);

        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPrice = txtEditPrice.getText().toString();
                String newStock = txtEditStock.getText().toString();

                if (TextUtils.isEmpty(newPrice) || TextUtils.isEmpty(newStock)) {
                    runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(EditConsoles.this, "Error", "Por favor completa todos los campos " ));
                 //   Toast.makeText(EditConsoles.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("price", newPrice);
                    requestData.put("stock", newStock);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();

                String editUrl = "http://173.255.204.68/api/gameconsoles/" + productId;

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());

                Request request = new Request.Builder()
                        .url(editUrl)
                        .put(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> {
                                        CustomAlert.showCustomSuccessDialog(EditConsoles.this, "¡Actualizacion exitosa!", "Producto actualizado exitosamente", EditConsoles.this);
                                    });
                                   // Toast.makeText(EditConsoles.this, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> CustomErrorAlert.showCustomErrorDialog(EditConsoles.this, "Error", "El stock no puede ser menor al inicial " ));
                                   // Toast.makeText(EditConsoles.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditConsoles.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    //Toolbar y metodos de cierre de sesion
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
        Intent intent = new Intent(EditConsoles.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(EditConsoles.this, Carrito.class);
        startActivity(intent);
    }
}
