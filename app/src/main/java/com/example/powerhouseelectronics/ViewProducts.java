package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class ViewProducts extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_view_products);

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
        LoadCpus();
        LoadConsoles();

        Button btnAddPhone = findViewById(R.id.btnAddPhone);
        Button btnAddConsole = findViewById(R.id.btnAddConsole);
        Button btnAddCpu = findViewById(R.id.btnAddCpu);

        btnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProducts.this, AddPhone.class);
                startActivity(intent);
            }
        });

        btnAddConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProducts.this, AddConsole.class);
                startActivity(intent);
            }
        });

        btnAddCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProducts.this, AddCpu.class);
                startActivity(intent);
            }
        });
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
        PhoneResponseList phoneResponseList = gson.fromJson(jsonData, PhoneResponseList.class);

        List<PhoneResponse> phoneList = phoneResponseList.getPhones();
        if (phoneList != null && !phoneList.isEmpty()) {
            for (PhoneResponse cellPhones : phoneList) {
                String phoneJson = gson.toJson(cellPhones);
                Log.d("PHONES_JSON", phoneJson);

                CardView phoneCard = GetProductsAdmin.createCardPhone(this, cellPhones);
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
        private List<ViewProducts.PhoneResponse> phones;

        public List<ViewProducts.PhoneResponse> getPhones() {
            return phones;
        }

        public void setProducts(List<ViewProducts.PhoneResponse> phones) {
            this.phones = phones;
        }
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
        CpuResponseList cpuResponseList = gson.fromJson(jsonData, CpuResponseList.class);

        List<CpuResponse> cpuList = cpuResponseList.getCpus();
        if (cpuList != null && !cpuList.isEmpty()) {
            for (CpuResponse CPUs : cpuList) {
                String cpuJson = gson.toJson(CPUs);
                Log.d("CPUS_JSON", cpuJson);

                CardView cpuCard = GetProductsAdmin.createCardCpu(this, CPUs);
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
        private List<ViewProducts.CpuResponse> cpus;

        public List<ViewProducts.CpuResponse> getCpus() {
            return cpus;
        }

        public void setProducts(List<ViewProducts.CpuResponse> cpus) {
            this.cpus = cpus;
        }
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
        ConsoleResponseList consoleResponseList = gson.fromJson(jsonData, ConsoleResponseList.class);

        List<ConsoleResponse> consoleList = consoleResponseList.getConsoles();
        if (consoleList != null && !consoleList.isEmpty()) {
            for (ConsoleResponse gameConsoles : consoleList) {
                String consoleJson = gson.toJson(gameConsoles);
                Log.d("CONSOLES_JSON", consoleJson);

                CardView consoleCard = GetProductsAdmin.createCardConsole(this, gameConsoles);
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
        private String features;

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

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
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
        private List<ViewProducts.ConsoleResponse> consoles;

        public List<ViewProducts.ConsoleResponse> getConsoles() {
            return consoles;
        }

        public void setProducts(List<ViewProducts.ConsoleResponse> consoles) {
            this.consoles = consoles;
        }
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
        } else if (item.getItemId() == R.id.profile) {
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

    private void GoProfile() {
        Intent intent = new Intent(ViewProducts.this, Profile.class);
        startActivity(intent);
    }

}



