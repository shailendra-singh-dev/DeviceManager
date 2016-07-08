package com.itexico.utilities.lockmydevice.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class LoginClient extends BaseClient {

    public interface ILoginClientService {
        @POST(DeviceSession.API_BASE_URL)
        Call authLogin();
    }

    private LoginClient() {
    }

    public static void performAuthLogin() {
        ILoginClientService iLoginClientService = create(ILoginClientService.class);
        Call call = iLoginClientService.authLogin();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
