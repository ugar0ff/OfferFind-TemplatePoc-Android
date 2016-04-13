package com.dddev.market.place.core.api.retrofit;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.api.strongloop.Opportunities;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ugar on 11.04.16.
 */
public class ApiRetrofit {

    private static Retrofit retrofit;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppOfferFind.API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static Call<List<Opportunities.ModelOpportunity>> getOpportunities(int id, String access_token) {
        OpportunitiesInterface opportunities = retrofit.create(OpportunitiesInterface.class);
        return opportunities.opportunitiesInfo(id, access_token);
    }

    public static Call<Messages> putMessage(int id, boolean read,  String access_token) {
        MessageInterface messages = retrofit.create(MessageInterface.class);
        return messages.changeRead(id, read, access_token);
    }

    private interface OpportunitiesInterface {

        @GET("/api/Accounts/{id}/opportunities?filter=%7B%22include%22%3A%20%7B%22bids%22%3A%20%5B%22owner%22%2C%20%22messages%22%5D%7D%7D")
        Call<List<Opportunities.ModelOpportunity>> opportunitiesInfo(@Path("id") int id, @Query("access_token") String access_token);
    }

    private interface MessageInterface {

        @FormUrlEncoded
        @PUT("/api/Messages/{id}")
        Call<Messages> changeRead(@Path("id") int id, @Field("read") boolean read, @Query("access_token") String access_token);
    }
}
