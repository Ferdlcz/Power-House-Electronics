package com.example.powerhouseelectronics;

import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

public class GetProductsAdmin {

    public static CardView createCardPhone(Context context, ViewProducts.PhoneResponse phone){

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

        return PhoneCardView;
    }
/*
    public static CardView createCardCpu(Context context, ViewProducts.PhoneResponse product){

        CardView productCardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        productCardView.setLayoutParams(cardParams);
        productCardView.setRadius(4);
        productCardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        productCardView.addView(cardLayout);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );

        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context)
                .load(product.getImage())
                .into(imageView);

        cardLayout.addView(imageView);

        TextView brandTextView = new TextView(context);
        brandTextView.setText("Marca: " + product.getBrand());
        brandTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(brandTextView);

        TextView modelTextView = new TextView(context);
        modelTextView.setText("Modelo: " + product.getModel());
        modelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(modelTextView);

        TextView colorTextView = new TextView(context);
        colorTextView.setText("Color: " + product.getColor());
        colorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(colorTextView);

        TextView storageTextView = new TextView(context);
        storageTextView.setText("Almacenamiento: " + product.getStorage());
        storageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(storageTextView);

        TextView priceTextView = new TextView(context);
        priceTextView.setText("Precio: " + product.getPrice());
        priceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(priceTextView);

        TextView screenresTextView = new TextView(context);
        screenresTextView.setText("Resolucion de pantalla: " + product.getScreenResolution());
        screenresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(screenresTextView);

        TextView cameraTextView = new TextView(context);
        cameraTextView.setText("Camara: " + product.getCameraResolution());
        cameraTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(cameraTextView);

        TextView stockTextView = new TextView(context);
        stockTextView.setText("Cantidad: " + product.getStock());
        stockTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(stockTextView);

        return productCardView;
    }*/

}
