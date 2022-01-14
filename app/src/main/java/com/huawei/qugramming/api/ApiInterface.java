package com.huawei.qugramming.api;

import com.huawei.qugramming.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("questions")
    Call<List<Question>> getQuestions(
            @Query("apiKey") String apiKey, @Query("category") String category, @Query("tag") String tag
    );
}
