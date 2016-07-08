package com.itexico.utilities.lockmydevice.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class BaseClient {

    protected static Retrofit getRetrofit() {
        DeviceSession session = DeviceSession.getInstance();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DeviceSession.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        final String authorizationCode = session.getAuthorizationCode();
        if ((authorizationCode != null) && (authorizationCode.length() > 0)) {
            OkHttpClient authHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("Authorization", authorizationCode)
                                            .build();

                                    return chain.proceed(request);
                                }
                            }
                    ).build();
            builder.client(authHttpClient);
        }

        return builder.build();
    }

    protected static <T> T create(Class<T> service) {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(service);
    }

    protected static <T> T create(Class<T> service, String apiBaseURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(service);
    }
}
