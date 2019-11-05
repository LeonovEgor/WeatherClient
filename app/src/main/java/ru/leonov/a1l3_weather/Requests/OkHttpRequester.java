package ru.leonov.a1l3_weather.Requests;


import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class OkHttpRequester {
    private static final String TAG = "WEATHER";
    private static final String OPEN_WEATHER_API_KEY = "1eb209182666b630fb58efb30a93cb00";
    private static final String OPEN_WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String KEY = "x-api-key";

    private OnResponseCompleted listener;

    OkHttpRequester(OnResponseCompleted listener) {
        this.listener = listener;
    }

    void run(String city) {
        URL url;
        try {
            url = new URL(String.format(OPEN_WEATHER_API_URL, city));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: call onError();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url).header(KEY, OPEN_WEATHER_API_KEY);
        Request request = builder.build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();

            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                if(response.isSuccessful()) {
                    final String answer = Objects.requireNonNull(response.body()).string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCompleted(answer);
                        }
                    });
                } else {
                    Log.e(TAG, "Запрос вернул ошибку: " + response.code());
                    final String err = String.valueOf(response.code());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(err);
                        }
                    });
                }
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Существенные проблемы связи: " + e.getMessage());

                final String err = e.getMessage();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(String.valueOf(err));
                    }
                });
            }
        });
    }

    // интерфейс обратного вызова, метод onCompleted вызывается по окончании загрузки страницы
    public interface OnResponseCompleted {
        void onCompleted(String content);
        void onError(String error);
    }
}