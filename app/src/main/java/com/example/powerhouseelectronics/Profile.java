package com.example.powerhouseelectronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String image = sharedPreferences.getString("image", "");
        String address = sharedPreferences.getString("address", "");
        String phone = sharedPreferences.getString("phone", "");

        TextView txtName = findViewById(R.id.TxtName);
        TextView txtEmail = findViewById(R.id.txtGmail);
        TextView txtAddress = findViewById(R.id.txtAdress);
        TextView txtPhone = findViewById(R.id.txtPhone);

        txtName.setText(name);
        txtEmail.setText(email);
        txtAddress.setText(address);
        txtPhone.setText(phone);

        CircleImageView imageView = findViewById(R.id.imageView3);
        Picasso.with(this)
                  .load(image)
                  .into(imageView);
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
            Intent intent = new Intent(Profile.this, Profile.class);
            startActivity(intent);
        }

}