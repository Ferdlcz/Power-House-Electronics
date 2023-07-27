package com.example.powerhouseelectronics;

import okhttp3.MultipartBody;
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

    @Multipart
    @POST("users/register")
    Call<Void> registerUserAdmin(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone,
            @Part("role") RequestBody role
    );

    @Multipart
    @POST("cellphones")
     Call<Void> registerPhone(
            @Part("brand") RequestBody brand,
            @Part("model") RequestBody model,
            @Part("color") RequestBody color,
            @Part("storage") RequestBody storage,
            @Part("price") RequestBody price,
            @Part("screenResolution") RequestBody screenResolution,
            @Part("cameraResolution") RequestBody cameraResolution,
            @Part MultipartBody.Part image
    );

}
