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

public class ViewCellPhonesAdmin extends AppCompatActivity implements ProductsFilterAdmin.OnDeleteClickListener<ViewCellPhonesAdmin.PhoneResponse>,
        ProductsFilterAdmin.OnEditClickListener<ViewCellPhonesAdmin.PhoneResponse> {

    Toolbar toolbar;
    LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_view_cell_phones_admin);
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

        LoadPhones();


    }

    //Solicitud a Celulares

    private void LoadPhones() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/cellphones")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarPhones(jsonData);
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

    private void mostrarPhones(String jsonData) {
        Gson gson = new Gson();
        ViewCellPhonesAdmin.PhoneResponseList phoneResponseList = gson.fromJson(jsonData, ViewCellPhonesAdmin.PhoneResponseList.class);

        List<ViewCellPhonesAdmin.PhoneResponse> phoneList = phoneResponseList.getPhones();
        if (phoneList != null && !phoneList.isEmpty()) {
            for (ViewCellPhonesAdmin.PhoneResponse cellPhones : phoneList) {
                String phoneJson = gson.toJson(cellPhones);
                Log.d("PHONES_JSON", phoneJson);

                CardView phoneCard = ProductsFilterAdmin.createCardPhone(this, cellPhones, this, this);
                productLayout.addView(phoneCard);
            }
        } else {
            Log.d("PHONES_JSON", "No hay telefonos disponibles");
        }

    }

    public class PhoneResponse implements Serializable {
        @SerializedName("_id")
        private String _id;

        @SerializedName("brand")
        private String brand;

        @SerializedName("model")
        private String model;

        @SerializedName("color")
        private String color;

        @SerializedName("storage")
        private String storage;

        @SerializedName("price")
        private String price;

        @SerializedName("screenResolution")
        private String screenResolution;

        @SerializedName("cameraResolution")
        private String cameraResolution;

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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

        public String getScreenResolution() {
            return screenResolution;
        }

        public void setScreenResolution(String screenResolution) {
            this.screenResolution = screenResolution;
        }

        public String getCameraResolution() {
            return cameraResolution;
        }

        public void setCameraResolution(String cameraResolution) {
            this.cameraResolution = cameraResolution;
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

    public class PhoneResponseList {
        @SerializedName("cellPhones")
        private List<ViewCellPhonesAdmin.PhoneResponse> phones;

        public List<ViewCellPhonesAdmin.PhoneResponse> getPhones() {
            return phones;
        }

        public void setProducts(List<ViewCellPhonesAdmin.PhoneResponse> phones) {
            this.phones = phones;
        }
    }


    public void onDeleteClicked(ViewCellPhonesAdmin.PhoneResponse phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación de Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePhone(phone.get_id());
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePhone(String consoleId) {
        OkHttpClient client = new OkHttpClient();

        String deleteUrl = "http://173.255.204.68/api/cellphones/" + consoleId;

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

    public void onEditClicked(ViewCellPhonesAdmin.PhoneResponse phone) {
        Intent intent = new Intent(this, EditCellphone.class);
        intent.putExtra("productId", phone.get_id());
        intent.putExtra("price", phone.getPrice());
        intent.putExtra("stock", phone.getStock());
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
        Intent intent = new Intent(ViewCellPhonesAdmin.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(ViewCellPhonesAdmin.this, Carrito.class);
        startActivity(intent);
    }

}