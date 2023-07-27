package com.example.powerhouseelectronics;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

public class GetUsers {

    public static CardView createCard(Context context, ViewUsers.UserResponse user, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(4);
        cardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(cardLayout);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context)
                //.load("https://i.kym-cdn.com/photos/images/original/002/559/519/e31.jpg")
                .load(user.getImage())
                .into(imageView);

        cardLayout.addView(imageView);

        // TextViews
        TextView nameTextView = new TextView(context);
        nameTextView.setText("Nombre: " + user.getName());
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(nameTextView);

        TextView emailTextView = new TextView(context);
        emailTextView.setText("Email: " + user.getEmail());
        emailTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(emailTextView);

        TextView addressTextView = new TextView(context);
        addressTextView.setText("Dirección: " + user.getAddress());
        addressTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(addressTextView);

        TextView phoneTextView = new TextView(context);
        phoneTextView.setText("Teléfono: " + user.getPhone());
        phoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(phoneTextView);

        TextView roleTextView = new TextView(context);
        roleTextView.setText("Rol: " + user.getRole());
        roleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        cardLayout.addView(roleTextView);

        // Edit Button
        Button editButton = new Button(context);
        editButton.setText("Editar");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClicked(user);
                }
            }
        });
        cardLayout.addView(editButton);

        // Delete Button
        Button deleteButton = new Button(context);
        deleteButton.setText("Eliminar");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClicked(user);
                }
            }
        });
        cardLayout.addView(deleteButton);
        return cardView;
    }

    public interface OnEditClickListener {
        void onEditClicked(ViewUsers.UserResponse user);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked(ViewUsers.UserResponse user);
    }
}
