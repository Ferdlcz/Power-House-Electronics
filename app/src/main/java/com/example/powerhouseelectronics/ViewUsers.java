package com.example.powerhouseelectronics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ViewUsers extends AppCompatActivity implements GetUsers.OnEditClickListener, GetUsers.OnDeleteClickListener {
    LinearLayout userLayout;
    Toolbar toolbar;

    public static final String EXTRA_USER_ID = "user_id";
    private static final int EDIT_USER_REQUEST_CODE = 1;

    private String[] rolesArray = {"admin", "user", "superadmin"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        Spinner roleSpinner = findViewById(R.id.role_spinner);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                rolesArray
        );

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roleSpinner.setAdapter(roleAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userLayout = findViewById(R.id.user_layout);

        loadUsersFromServer();

        Button btnRedireccionRegistro = findViewById(R.id.btnRegUser);

        btnRedireccionRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ViewUsers.this, AdminUserRegister.class);
                startActivity(intent);
            }
        });
    }

    private void loadUsersFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/users/admin")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarUsuarios(jsonData);
                        }
                    });
                } else {
                    // Aquí puedes manejar el caso en el que la respuesta no sea exitosa
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarUsuarios(String jsonData) {
        Gson gson = new Gson();
        UserResponseList userResponseList = gson.fromJson(jsonData, UserResponseList.class);

        List<UserResponse> userList = userResponseList.getUsers();
        for (UserResponse user : userList) {
            String userJson = gson.toJson(user);
            Log.d("USER_JSON", userJson);

            CardView userCard = GetUsers.createCard(this, user, this, this);

            userLayout.addView(userCard);
        }
    }

    public void onEditClicked(ViewUsers.UserResponse user) {
        Intent intent = new Intent(this, EditUser.class);
        intent.putExtra("user_data", user);
        intent.putExtra(EXTRA_USER_ID, user.get_id());
        startActivityForResult(intent, EDIT_USER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Actualiza la lista de usuarios cuando se complete la edición del usuario
            loadUsersFromServer();
        }
    }

    public void onDeleteClicked(ViewUsers.UserResponse user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Usuario");
        builder.setMessage("¿Está seguro de que desea eliminar este usuario?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(user.get_id());
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        String modifierId = sharedPreferences.getString("id", "");

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String jsonBody = "{\"modifierId\":\"" + modifierId + "\"}";

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url("http://173.255.204.68/api/users/" + userId)
                .delete(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("USER_DELETE", "Usuario eliminado correctamente");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadUsersFromServer();
                        }
                    });
                } else {
                    Log.d("USER_DELETE", "Error al eliminar usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("USER_DELETE", "Error en la solicitud: " + e.getMessage());
            }
        });
    }

    public class UserResponse implements Serializable {
        @SerializedName("_id")
        private String _id;
        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("role")
        private String role;

        @SerializedName("address")
        private String address;

        @SerializedName("image")
        private String image;

        @SerializedName("phone")
        private String phone;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public class UserResponseList {
        @SerializedName("users")
        private List<ViewUsers.UserResponse> users;

        public List<ViewUsers.UserResponse> getUsers() {
            return users;
        }

        public void setUsers(List<ViewUsers.UserResponse> users) {
            this.users = users;
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
        Intent intent = new Intent(ViewUsers.this, Profile.class);
        startActivity(intent);
    }
}