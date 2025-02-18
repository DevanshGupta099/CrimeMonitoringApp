package com.example.crime_monitoring_app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/process_file")
    Call<ResponseBody> processFile(
            @Header("x-api-key") String apiKey,
            @Part MultipartBody.Part file
    );
}