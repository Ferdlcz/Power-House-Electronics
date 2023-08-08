package com.example.powerhouseelectronics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Carrito extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_carrito);

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

        SharedPreferences carritoItem = getSharedPreferences("CartItems", MODE_PRIVATE);
        String productListJson = carritoItem.getString("CartItemsList", "");

        if (!productListJson.isEmpty()) {
            Gson gson = new Gson();
            ArrayList<Product> productList = gson.fromJson(productListJson, new TypeToken<ArrayList<Product>>(){}.getType());

            TableLayout tableLayout = findViewById(R.id.tableLayout);

            for (Product product : productList) {
                TableRow row = new TableRow(this);

                TextView brandTextView = new TextView(this);
                brandTextView.setText(product.getBrand());
                brandTextView.setTextColor(Color.BLACK);
                brandTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(100)));
                brandTextView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(brandTextView);

                TextView modelTextView = new TextView(this);
                modelTextView.setText(product.getModel());
                modelTextView.setTextColor(Color.BLACK);
                modelTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(100)));
                modelTextView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(modelTextView);

                TextView priceTextView = new TextView(this);
                priceTextView.setText(product.getPrice());
                priceTextView.setTextColor(Color.BLACK);
                priceTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(100)));
                priceTextView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(priceTextView);

                ImageView productImageView = new ImageView(this);
                productImageView.setLayoutParams(new TableRow.LayoutParams((20), (100)));
                productImageView.setMaxWidth(20);
                productImageView.setMaxHeight(20);
                productImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Picasso.with(this)
                        .load(product.getImage())
                        .into(productImageView);
                productImageView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(productImageView);

                tableLayout.addView(row);
            }
        }

        Button clearCartButton = findViewById(R.id.ClearCart);

        clearCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("CartItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("CartItemsList");
                editor.apply();

                Intent intent = new Intent(Carrito.this, Index.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buyAllButton = findViewById(R.id.BuyAll);
        buyAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                String shippingAddress = sharedPreferences.getString("address", "");

                SharedPreferences cartItemsSharedPreferences = getSharedPreferences("CartItems", MODE_PRIVATE);
                String productListJson = cartItemsSharedPreferences.getString("CartItemsList", "");
                ArrayList<Product> productList = new Gson().fromJson(productListJson, new TypeToken<ArrayList<Product>>(){}.getType());

                ArrayList<Map<String, Object>> productsList = new ArrayList<>();
                for (Product product : productList) {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("product", product.getId());
                    productMap.put("quantity", 1);
                    productsList.add(productMap);
                }

                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("token", token);
                requestBody.put("products", productsList);
                requestBody.put("shippingAddress", shippingAddress);

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, new Gson().toJson(requestBody));
                Request request = new Request.Builder()
                        .url("http://173.255.204.68/api/orders")
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            cartItemsSharedPreferences.edit().remove("CartItemsList").apply();
                            Intent intent = new Intent(Carrito.this, Index.class);
                            startActivity(intent);
                        } else {
                        }
                    }
                });
            }
        });
    }

    public static void addToCart(Context context, Index.Phone phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String cartItemsJson = sharedPreferences.getString("CartItemsList", "");
        ArrayList<Index.Phone> cartItemsList = new ArrayList<>();
        if (!cartItemsJson.isEmpty()) {
            Type type = new TypeToken<ArrayList<Index.Phone>>(){}.getType();
            cartItemsList = gson.fromJson(cartItemsJson, type);
        }

        cartItemsList.add(phone);
        String updatedCartItemsJson = gson.toJson(cartItemsList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CartItemsList", updatedCartItemsJson);
        editor.apply();
    }
    public static ArrayList<Index.Phone> getCartItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
        String cartItemsJson = sharedPreferences.getString("CartItemsList", "");
        Gson gson = new Gson();

        ArrayList<Index.Phone> cartItemsList = new ArrayList<>();
        if (!cartItemsJson.isEmpty()) {
            Type type = new TypeToken<ArrayList<Index.Phone>>(){}.getType();
            cartItemsList = gson.fromJson(cartItemsJson, type);
        }

        return cartItemsList;
    }

    //MENU Y OTROS METODOS
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
        SharedPreferences CartItem = getSharedPreferences("CartItems", Context.MODE_PRIVATE);
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
        Intent intent = new Intent(Carrito.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(Carrito.this, Carrito.class);
        startActivity(intent);
    }
}

