package com.example.powerhouseelectronics;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
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
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone
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
            @Part("stock")RequestBody stock,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("gameconsoles")
    Call<Void> registerConsole(
            @Part("brand") RequestBody brand,
            @Part("model") RequestBody model,
            @Part("storage") RequestBody storage,
            @Part("price") RequestBody price,
            @Part("features") RequestBody features,
            @Part("color") RequestBody color,
            @Part("stock")RequestBody stock,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("cpus")
    Call<Void> registerCpu(
            @Part("brand") RequestBody brand,
            @Part("model") RequestBody model,
            @Part("processor") RequestBody processor,
            @Part("ram") RequestBody ram,
            @Part("storage") RequestBody storage,
            @Part("price") RequestBody price,
            @Part("operatingSystem")RequestBody operatingSystem,
            @Part("graphicsCard")RequestBody graphicsCard,
            @Part("stock")RequestBody stock,
            @Part MultipartBody.Part image
    );

}
