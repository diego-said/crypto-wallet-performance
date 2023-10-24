package br.com.doublelogic.cryptowalletperformance.integration;

import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetHistoryData;
import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetHistoryEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CoincapAPI {

    @Value(value = "${coincap.api.base.url}")
    private String baseURL;

    @Value(value = "${coincap.api.timeout}")
    private int timeout;

    @Value(value = "${coincap.api.interval}")
    private String historyInterval;

    @Value(value = "${coincap.api.start}")
    private String historyStart;

    @Value(value = "${coincap.api.end}")
    private String historyEnd;

    public Optional<AssetHistoryEntity> getAssetHistory(String assetId) {
        Response response = requestAssetHistory(assetId);
        if(response.code() == HttpURLConnection.HTTP_OK) {
            Optional<AssetHistoryData> assetHistoryData = convertAssetHistoryResponseBody(response.body());
            if(assetHistoryData.get().getData().size() > 0) {
                return Optional.ofNullable(assetHistoryData.get().getData().get(0));
            }
        }
        return Optional.empty();
    }

    private Response requestAssetHistory(String assetId) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(baseURL + "assets").newBuilder();
        urlBuilder.addPathSegment(assetId);
        urlBuilder.addPathSegment("history");
        urlBuilder.addQueryParameter("interval", historyInterval);
        urlBuilder.addQueryParameter("start", historyStart);
        urlBuilder.addQueryParameter("end", historyEnd);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();

        String assetHistoryURL = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(assetHistoryURL)
                .build();

        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<AssetHistoryData> convertAssetHistoryResponseBody(ResponseBody responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AssetHistoryData assetHistoryData = objectMapper.readValue(responseBody.string(), AssetHistoryData.class);
            return Optional.ofNullable(assetHistoryData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
