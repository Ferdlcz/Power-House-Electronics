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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Index extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_index);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productLayout = findViewById(R.id.product_layout);

        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", "Nombre de usuario");
        TextView userNameTextView = findViewById(R.id.user_name);
        userNameTextView.setText(userName);

        Button btnViewPhone = findViewById(R.id.btnViewTelefono);
        Button btnViewConsole = findViewById(R.id.btnViewConsola);
        Button btnViewCpu = findViewById(R.id.btnViewPC);


        CircleImageView userProfileImageView = findViewById(R.id.user_profile_image);

        String userImageURL = sharedPreferences.getString("image", "");

        Picasso.with(this)
                .load(userImageURL)
                .into(userProfileImageView);

        btnViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this, ViewCellPhones.class);
                startActivity(intent);
            }
        });

        btnViewConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this, ViewConsoles.class);
                startActivity(intent);
            }
        });

        btnViewCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this, ViewComputers.class);
                startActivity(intent);
            }
        });

        LoadProducts();
    }


    private void LoadProducts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/products")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProducts(jsonData);
                        }
                    });
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure
            }
        });
    }

    private void mostrarProducts(String jsonData) {
        Gson gson = new Gson();
        Index.ProductResponseList productResponseList = gson.fromJson(jsonData, Index.ProductResponseList.class);

        List<Index.Product> productList = new ArrayList<>();

        if (productResponseList != null) {
            List<Index.Phone> phones = productResponseList.getPhones();
            List<Index.CPU> cpus = productResponseList.getCpus();
            List<Index.Console> consoles = productResponseList.getConsoles();

            if (phones != null) {
                productList.addAll(phones);
            }

            if (cpus != null) {
                productList.addAll(cpus);
            }

            if (consoles != null) {
                productList.addAll(consoles);
            }
        }

        if (!productList.isEmpty()) {
            for (Index.Product product : productList) {
                String productJson = gson.toJson(product);
                Log.d("PRODUCTS_JSON", productJson);

                CardView productCard = createProductCard(product);
                productLayout.addView(productCard);
            }
        } else {
            Log.d("PRODUCTS_JSON", "No hay productos disponibles");
        }
    }

    private CardView createProductCard(Index.Product product) {
        if (product instanceof Index.Phone) {
            return GetProductsClient.createCardPhone(this, (Index.Phone) product);
        } else if (product instanceof Index.CPU) {
            return GetProductsClient.createCardCpu(this, (Index.CPU) product);
        } else if (product instanceof Index.Console) {
            return GetProductsClient.createCardConsole(this, (Index.Console) product);
        } else {
            return new CardView(this);
        }
    }

    private void LoadPhones() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/products")
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
        Index.ProductResponseList phoneResponseList = gson.fromJson(jsonData, Index.ProductResponseList.class);

        List<Index.Phone> phoneList = phoneResponseList.getPhones();
        if (phoneList != null && !phoneList.isEmpty()) {
            for (Index.Phone phone : phoneList) {
                String phoneJson = gson.toJson(phone);
                Log.d("PHONES_JSON", phoneJson);

                CardView phoneCard = GetProductsClient.createCardPhone(this, phone);
                productLayout.addView(phoneCard);
            }
        } else {
            Log.d("PHONES_JSON", "No hay telefonos disponibles");
        }
    }


    private void LoadCpus() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/products")
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
        Index.ProductResponseList cpuResponseList = gson.fromJson(jsonData, Index.ProductResponseList.class);

        List<Index.CPU> cpuList = cpuResponseList.getCpus();
        if (cpuList != null && !cpuList.isEmpty()) {
            for (Index.CPU cpu : cpuList) {
                String cpuJson = gson.toJson(cpu);
                Log.d("CPUS_JSON", cpuJson);

                CardView cpuCard = GetProductsClient.createCardCpu(this, cpu);
                productLayout.addView(cpuCard);
            }
        } else {
            Log.d("CPUS_JSON", "No hay computadoras disponibles");
        }
    }

    private void LoadConsoles(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/products")
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

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<String>>() {}.getType(), new Index())
                .create();

        Index.ProductResponseList consoleResponseList = gson.fromJson(jsonData, Index.ProductResponseList.class);

        List<Index.Console> consoleList = consoleResponseList.getConsoles();
        if (consoleList != null && !consoleList.isEmpty()) {
            for (Index.Console console : consoleList) {
                String consoleJson = gson.toJson(console);
                Log.d("CONSOLES_JSON", consoleJson);

                CardView consoleCard = GetProductsClient.createCardConsole(this, console);
                productLayout.addView(consoleCard);
            }
        } else {
            Log.d("CONSOLES_JSON", "No hay consolas disponibles");
        }
    }


    public class Product implements Serializable {
        private String _id;
        private String brand;
        private String model;
        private String storage;
        private String image;
        private String price;
        private int stock;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    public class Phone extends Index.Product {
        private String color;
        private String screenResolution;
        private String cameraResolution;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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
    }

    public class CPU extends Index.Product {
        private String processor;
        private String ram;
        private String operatingSystem;
        private String graphicsCard;

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
    }

    public class Console extends Index.Product {
        @SerializedName("features")
        private List <String> features;
        private String color;

        public List <String> getFeatures() {
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
    }

    public class FeaturesDeserializer implements JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<String> featuresList = new ArrayList<>();
            if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    featuresList.add(element.getAsString());
                }
            } else if (json.isJsonPrimitive()) {
                featuresList.add(json.getAsString());
            }
            return featuresList;
        }
    }

    public class ProductResponseList {
        @SerializedName("cellphones")
        private List<Index.Phone> phones;

        @SerializedName("computers")
        private List<Index.CPU> cpus;

        @SerializedName("gconsoles")
        private List<Index.Console> consoles;

        public List<Index.Phone> getPhones() {
            return phones;
        }

        public void setPhones(List<Index.Phone> phones) {
            this.phones = phones;
        }

        public List<Index.CPU> getCpus() {
            return cpus;
        }

        public void setCpus(List<Index.CPU> cpus) {
            this.cpus = cpus;
        }

        public List<Index.Console> getConsoles() {
            return consoles;
        }

        public void setConsoles(List<Index.Console> consoles) {
            this.consoles = consoles;
        }
    }


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

    private void GoProfile (){
        Intent intent = new Intent(Index.this, Profile.class);
        startActivity(intent);
    }

}