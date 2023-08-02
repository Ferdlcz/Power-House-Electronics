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

public class ViewComputersAdmin extends AppCompatActivity implements ProductsFilterAdmin.OnDeleteClickListener<ViewComputersAdmin.CpuResponse>{

    Toolbar toolbar;
    LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_view_computers_admin);

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

        LoadCpus();
    }

    //Solicitud a Computadoras

    private void LoadCpus() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/cpus")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarCpus(jsonData);
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

    private void mostrarCpus(String jsonData) {
        Gson gson = new Gson();
        ViewComputersAdmin.CpuResponseList cpuResponseList = gson.fromJson(jsonData, ViewComputersAdmin.CpuResponseList.class);

        List<ViewComputersAdmin.CpuResponse> cpuList = cpuResponseList.getCpus();
        if (cpuList != null && !cpuList.isEmpty()) {
            for (ViewComputersAdmin.CpuResponse CPUs : cpuList) {
                String cpuJson = gson.toJson(CPUs);
                Log.d("CPUS_JSON", cpuJson);

                CardView cpuCard = ProductsFilterAdmin.createCardCpu(this, CPUs, this);
                productLayout.addView(cpuCard);
            }
        } else {
            Log.d("CPUS_JSON", "No hay telefonos disponibles");
        }

    }

    public class CpuResponse implements Serializable {

        @SerializedName("_id")
        private String _id;

        @SerializedName("brand")
        private String brand;

        @SerializedName("model")
        private String model;

        @SerializedName("processor")
        private String processor;

        @SerializedName("ram")
        private String ram;

        @SerializedName("storage")
        private String storage;

        @SerializedName("price")
        private String price;

        @SerializedName("operatingSystem")
        private String operatingSystem;

        @SerializedName("graphicsCard")
        private String graphicsCard;

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

        public String getProcessor() {
            return processor;
        }

        public void setProcessor(String processor) {
            this.processor = processor;
        }

        public String getRam() {
            return ram;
        }

        public void setRam(String ram) {
            this.ram = ram;
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

        public String getOperatingSystem() {
            return operatingSystem;
        }

        public void setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
        }

        public String getGraphicsCard() {
            return graphicsCard;
        }

        public void setGraphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
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

    public class CpuResponseList {
        @SerializedName("CPUs")
        private List<ViewComputersAdmin.CpuResponse> cpus;

        public List<ViewComputersAdmin.CpuResponse> getCpus() {
            return cpus;
        }

        public void setProducts(List<ViewComputersAdmin.CpuResponse> cpus) {
            this.cpus = cpus;
        }
    }

    public void onDeleteClicked(ViewComputersAdmin.CpuResponse cpu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación de Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCpu(cpu.get_id());
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCpu(String consoleId) {
        OkHttpClient client = new OkHttpClient();

        String deleteUrl = "http://173.255.204.68/api/cpus/" + consoleId;

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
        Intent intent = new Intent(ViewComputersAdmin.this, Profile.class);
        startActivity(intent);
    }

    private void GoCarrito (){
        Intent intent = new Intent(ViewComputersAdmin.this, Carrito.class);
        startActivity(intent);
    }
}