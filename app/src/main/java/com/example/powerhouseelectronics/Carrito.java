package com.example.powerhouseelectronics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
                brandTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(100)));
                brandTextView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(brandTextView);

                TextView modelTextView = new TextView(this);
                modelTextView.setText(product.getModel());
                modelTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(100)));
                modelTextView.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(modelTextView);

                TextView priceTextView = new TextView(this);
                priceTextView.setText(product.getPrice());
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
    }
/*
        SharedPreferences CarritoItem = getSharedPreferences("CartItems", MODE_PRIVATE);
        String productJson = CarritoItem.getString("CartItem", "");

        Gson gson = new Gson();
        Product product = gson.fromJson(productJson, Product.class);

        TextView brandTextView = findViewById(R.id.brandTextView);
        brandTextView.setText(product.getBrand());

        TextView modelTextView = findViewById(R.id.modelTextView);
        modelTextView.setText(product.getModel());

        TextView priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText(product.getPrice());

        ImageView productImageView = findViewById(R.id.productImageView);
        Picasso.with(this)
                .load(product.getImage())
                .into(productImageView);*/


    public static void addToCart(Context context, Index.Phone phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Obtener la lista actual de productos del carrito desde SharedPreferences
        String cartItemsJson = sharedPreferences.getString("CartItemsList", "");
        ArrayList<Index.Phone> cartItemsList = new ArrayList<>();
        if (!cartItemsJson.isEmpty()) {
            Type type = new TypeToken<ArrayList<Index.Phone>>(){}.getType();
            cartItemsList = gson.fromJson(cartItemsJson, type);
        }

        // Agregar el nuevo producto a la lista
        cartItemsList.add(phone);

        // Convertir la lista actualizada a JSON y guardarla en SharedPreferences
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
        Intent intent = new Intent(Carrito.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(Carrito.this, Carrito.class);
        startActivity(intent);
    }
}

