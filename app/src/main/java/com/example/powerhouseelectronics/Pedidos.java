package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pedidos extends AppCompatActivity {

    Toolbar toolbar;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_pedidos);

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

        tableLayout = findViewById(R.id.table_layout);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/orders")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    try {
                        JSONArray ordersArray = new JSONArray(responseData);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Crear encabezado de tabla
                                TableRow headerRow = new TableRow(Pedidos.this);
                                TextView idHeader = new TextView(Pedidos.this);
                                idHeader.setText("ID");
                                TextView totalHeader = new TextView(Pedidos.this);
                                totalHeader.setText("Total");
                                TextView statusHeader = new TextView(Pedidos.this);
                                statusHeader.setText("Status");
                                headerRow.addView(idHeader);
                                headerRow.addView(totalHeader);
                                headerRow.addView(statusHeader);
                                tableLayout.addView(headerRow);

                                // Recorrer el array de pedidos y agregar filas a la tabla
                                for (int i = 0; i < ordersArray.length(); i++) {
                                    try {
                                        JSONObject order = ordersArray.getJSONObject(i);
                                        String orderId = order.getString("_id");
                                        String orderTotal = order.getString("total");
                                        String orderStatus = order.getString("status");

                                        TableRow row = new TableRow(Pedidos.this);
                                        TextView idTextView = new TextView(Pedidos.this);
                                        idTextView.setText(orderId);
                                        TextView totalTextView = new TextView(Pedidos.this);
                                        totalTextView.setText(orderTotal);
                                        TextView statusTextView = new TextView(Pedidos.this);
                                        statusTextView.setText(orderStatus);

                                        row.addView(idTextView);
                                        row.addView(totalTextView);
                                        row.addView(statusTextView);

                                        tableLayout.addView(row);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Manejo de error en caso de respuesta no exitosa
                }
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
        Intent intent = new Intent(Pedidos.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(Pedidos.this, Carrito.class);
        startActivity(intent);
    }
}