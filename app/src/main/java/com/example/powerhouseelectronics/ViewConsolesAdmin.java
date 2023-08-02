package com.example.powerhouseelectronics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewConsolesAdmin extends AppCompatActivity  implements ProductsFilterAdmin.OnDeleteClickListener<ViewConsolesAdmin.ConsoleResponse>,
        ProductsFilterAdmin.OnEditClickListener<ViewConsolesAdmin.ConsoleResponse> {

    Toolbar toolbar;
    LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_view_consoles_admin);

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

        LoadConsoles();
    }


    //SOLICITUD DE CONSOLAS

    private void LoadConsoles(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/gameconsoles")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarConsoles(jsonData);
                        }
                    });
                } else {

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarConsoles(String jsonData) {
        Gson gson = new Gson();
        ViewConsolesAdmin.ConsoleResponseList consoleResponseList = gson.fromJson(jsonData, ViewConsolesAdmin.ConsoleResponseList.class);

        List<ConsoleResponse> consoleList = consoleResponseList.getConsoles();
        if (consoleList != null && !consoleList.isEmpty()) {
            for (ViewConsolesAdmin.ConsoleResponse gameConsoles : consoleList) {
                String consoleJson = gson.toJson(gameConsoles);
                Log.d("CONSOLES_JSON", consoleJson);

                CardView consoleCard = ProductsFilterAdmin.createCardConsole(this, gameConsoles, this, this);
                productLayout.addView(consoleCard);
            }
        } else {
            Log.d("CPUS_JSON", "No hay telefonos disponibles");
        }

    }

    public class ConsoleResponse implements Serializable {

        @SerializedName("_id")
        private String _id;

        @SerializedName("brand")
        private String brand;

        @SerializedName("model")
        private String model;

        @SerializedName("storage")
        private String storage;

        @SerializedName("price")
        private String price;

        @SerializedName("features")
        private List<String> features;

        @SerializedName("color")
        private String color;

        @SerializedName("stock")
        private String stock;

        @SerializedName("image")
        private String image;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }
        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class ConsoleResponseList {
        @SerializedName("gameConsoles")
        private List<ViewConsolesAdmin.ConsoleResponse> consoles;

        public List<ViewConsolesAdmin.ConsoleResponse> getConsoles() {
            return consoles;
        }

        public void setProducts(List<ViewConsolesAdmin.ConsoleResponse> consoles) {
            this.consoles = consoles;
        }
    }

    public void onDeleteClicked(ViewConsolesAdmin.ConsoleResponse console) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación de Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteConsole(console.get_id());
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteConsole(String consoleId) {
        OkHttpClient client = new OkHttpClient();

        String deleteUrl = "http://173.255.204.68/api/gameconsoles/" + consoleId;

        Request request = new Request.Builder()
                .url(deleteUrl)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // productLayout.removeView(cardView);
                        }
                    });
                } else {
                    Log.e("DELETE_CONSOLE", "Error al eliminar la consola");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DELETE_CONSOLE", "Error en la solicitud DELETE", e);
            }
        });
    }

    public void onEditClicked(ViewConsolesAdmin.ConsoleResponse console) {
        Intent intent = new Intent(this, EditConsoles.class);
        intent.putExtra("productId", console.get_id());
        intent.putExtra("price", console.getPrice());
        intent.putExtra("stock", console.getStock());
        startActivity(intent);
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
        Intent intent = new Intent(ViewConsolesAdmin.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(ViewConsolesAdmin.this, Carrito.class);
        startActivity(intent);
    }
}
