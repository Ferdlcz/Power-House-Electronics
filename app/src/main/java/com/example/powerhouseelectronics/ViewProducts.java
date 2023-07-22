package com.example.powerhouseelectronics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProducts extends AppCompatActivity {

    Toolbar toolbar;

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

        Button btnAddPhone = findViewById(R.id.btnAddPhone);

        btnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProducts.this, AddPhone.class);
                startActivity(intent);
            }
        });
    }
}