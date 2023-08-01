package com.example.powerhouseelectronics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GetProductsClient {
    public static CardView createCardPhone(Context context, Index.Phone phone){

        CardView PhoneCardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        PhoneCardView.setLayoutParams(cardParams);
        PhoneCardView.setRadius(4);
        PhoneCardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        PhoneCardView.addView(cardLayout);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );

        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context)
                .load(phone.getImage())
                .into(imageView);

        cardLayout.addView(imageView);

        TextView brandTextView = new TextView(context);
        brandTextView.setText("Marca: " + phone.getBrand());
        brandTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(brandTextView);

        TextView modelTextView = new TextView(context);
        modelTextView.setText("Modelo: " + phone.getModel());
        modelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(modelTextView);

        TextView colorTextView = new TextView(context);
        colorTextView.setText("Color: " + phone.getColor());
        colorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(colorTextView);

        TextView storageTextView = new TextView(context);
        storageTextView.setText("Almacenamiento: " + phone.getStorage());
        storageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(storageTextView);

        TextView priceTextView = new TextView(context);
        priceTextView.setText("Precio: " + phone.getPrice());
        priceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(priceTextView);

        TextView screenresTextView = new TextView(context);
        screenresTextView.setText("Resolucion de pantalla: " + phone.getScreenResolution());
        screenresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(screenresTextView);

        TextView cameraTextView = new TextView(context);
        cameraTextView.setText("Camara: " + phone.getCameraResolution());
        cameraTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(cameraTextView);

        TextView stockTextView = new TextView(context);
        stockTextView.setText("Cantidad: " + phone.getStock());
        stockTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(stockTextView);

        Button addToCartButton = new Button(context);
        addToCartButton.setText("Añadir al carrito");
        cardLayout.addView(addToCartButton);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String productListJson = sharedPreferences.getString("CartItemsList", "");
                ArrayList<Product> productList;
                if (!productListJson.isEmpty()) {
                    productList = gson.fromJson(productListJson, new TypeToken<ArrayList<Product>>(){}.getType());
                } else {
                    productList = new ArrayList<>();
                }

                Product product = new Product(phone.get_id() ,phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getImage());

                productList.add(product);

                String productListUpdatedJson = gson.toJson(productList);
                editor.putString("CartItemsList", productListUpdatedJson);
                editor.apply();

                Intent intent = new Intent(context, Carrito.class);
                context.startActivity(intent);
            }
        });
        return PhoneCardView;
    }

    //CARDS DE COMPUTADORAS
    public static CardView createCardCpu(Context context, Index.CPU cpu){

        CardView CpuCardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        CpuCardView.setLayoutParams(cardParams);
        CpuCardView.setRadius(4);
        CpuCardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        CpuCardView.addView(cardLayout);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );

        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context)
                .load(cpu.getImage())
                .into(imageView);

        cardLayout.addView(imageView);

        TextView CpubrandTextView = new TextView(context);
        CpubrandTextView.setText("Marca: " + cpu.getBrand());
        CpubrandTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpubrandTextView);

        TextView CpumodelTextView = new TextView(context);
        CpumodelTextView.setText("Modelo: " + cpu.getModel());
        CpumodelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpumodelTextView);

        TextView CpuProcessorTextView = new TextView(context);
        CpuProcessorTextView.setText("Procesador: " + cpu.getProcessor());
        CpuProcessorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuProcessorTextView);

        TextView CpuRamTextView = new TextView(context);
        CpuRamTextView.setText("Memoria Ram: " + cpu.getRam());
        CpuRamTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuRamTextView);

        TextView CpuStorageTextView = new TextView(context);
        CpuStorageTextView.setText("Almacenamiento: " + cpu.getStorage());
        CpuStorageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuStorageTextView);

        TextView CpuPriceTextView = new TextView(context);
        CpuPriceTextView.setText("Precio: " + cpu.getPrice());
        CpuPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuPriceTextView);

        TextView CpuOperatingSystemTextView = new TextView(context);
        CpuOperatingSystemTextView.setText("Sistema Operativo: " + cpu.getOperatingSystem());
        CpuOperatingSystemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuOperatingSystemTextView);

        TextView CpuGraphicsCardTextView = new TextView(context);
        CpuGraphicsCardTextView.setText("Tarjeta Grafica: " + cpu.getGraphicsCard());
        CpuGraphicsCardTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuGraphicsCardTextView);

        TextView CpuStockTextView = new TextView(context);
        CpuStockTextView.setText("Cantidad: " + cpu.getStock());
        CpuStockTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(CpuStockTextView);

        Button addToCartButton = new Button(context);
        addToCartButton.setText("Añadir al carrito");
        cardLayout.addView(addToCartButton);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String productListJson = sharedPreferences.getString("CartItemsList", "");
                ArrayList<Product> productList;
                if (!productListJson.isEmpty()) {
                    productList = gson.fromJson(productListJson, new TypeToken<ArrayList<Product>>(){}.getType());
                } else {
                    productList = new ArrayList<>();
                }

                Product product = new Product(cpu.get_id() ,cpu.getBrand(), cpu.getModel(), cpu.getPrice(), cpu.getImage());

                productList.add(product);

                String productListUpdatedJson = gson.toJson(productList);
                editor.putString("CartItemsList", productListUpdatedJson);
                editor.apply();

                Intent intent = new Intent(context, Carrito.class);
                context.startActivity(intent);
            }
        });

        return CpuCardView;
    }

    //CARD CONSOLES

    public static CardView createCardConsole(Context context, Index.Console console) {
        CardView ConsoleCardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        ConsoleCardView.setLayoutParams(cardParams);
        ConsoleCardView.setRadius(4);
        ConsoleCardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        ConsoleCardView.addView(cardLayout);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );

        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context)
                .load(console.getImage())
                .into(imageView);

        cardLayout.addView(imageView);

        TextView ConsolebrandTextView = new TextView(context);
        ConsolebrandTextView.setText("Marca: " + console.getBrand());
        ConsolebrandTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsolebrandTextView);

        TextView ConsolemodelTextView = new TextView(context);
        ConsolemodelTextView.setText("Modelo: " + console.getModel());
        ConsolemodelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsolemodelTextView);

        TextView ConsoleStorageTextView = new TextView(context);
        ConsoleStorageTextView.setText("Almacenamiento: " + console.getStorage());
        ConsoleStorageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsoleStorageTextView);

        TextView ConsolePriceTextView = new TextView(context);
        ConsolePriceTextView.setText("Precio: " + console.getPrice());
        ConsolePriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsolePriceTextView);

        TextView ConsoleFeaturesTextView = new TextView(context);
        ConsoleFeaturesTextView.setText("Características: " + console.getFeatures());
        ConsoleFeaturesTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsoleFeaturesTextView);

        TextView ConsoleColorTextView = new TextView(context);
        ConsoleColorTextView.setText("Color: " + console.getColor());
        ConsoleColorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsoleColorTextView);

        TextView ConsoleStockSystemTextView = new TextView(context);
        ConsoleStockSystemTextView.setText("Cantidad: " + console.getStock());
        ConsoleStockSystemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(ConsoleStockSystemTextView);

        Button addToCartButton = new Button(context);
        addToCartButton.setText("Añadir al carrito");
        cardLayout.addView(addToCartButton);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String productListJson = sharedPreferences.getString("CartItemsList", "");
                ArrayList<Product> productList;
                if (!productListJson.isEmpty()) {
                    productList = gson.fromJson(productListJson, new TypeToken<ArrayList<Product>>(){}.getType());
                } else {
                    productList = new ArrayList<>();
                }

                Product product = new Product(console.get_id() ,console.getBrand(), console.getModel(), console.getPrice(), console.getImage());

                productList.add(product);

                String productListUpdatedJson = gson.toJson(productList);
                editor.putString("CartItemsList", productListUpdatedJson);
                editor.apply();

                Intent intent = new Intent(context, Carrito.class);
                context.startActivity(intent);
            }
        });
        return ConsoleCardView;
    }
}
