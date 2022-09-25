package jjfactory.simpleapi.global.config.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@NoArgsConstructor
@Configuration
public class RetrofitConfig<T>{
    @Value("${retrofitUrl}")
    private String baseUrl;

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private final OkHttpClient httpClient = OkHttpClients.getUnsafeOkHttpClient();

    private retrofit2.Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build();

    public <T> T create(Class<T> sClass){
        return retrofit.create(sClass);
    }

}
