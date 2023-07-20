package com.example.powerhouseelectronics;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    @Multipart
    @POST("users/register")
    Call<Void> registerUser(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone
    );
}
