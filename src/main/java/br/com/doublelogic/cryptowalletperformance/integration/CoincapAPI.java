package br.com.doublelogic.cryptowalletperformance.integration;

import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetData;
import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetEntity;
import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetHistoryData;
import br.com.doublelogic.cryptowalletperformance.integration.entities.AssetHistoryEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
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

    public Optional<AssetEntity> getAsset(String symbol) {
        Response response = requestAsset(symbol);
        if(response.code() == HttpURLConnection.HTTP_OK) {
            Optional<AssetData> assetData = convertAssetResponseBody(response.body());
            if(assetData.isPresent())
                return assetData.get().getData().stream().filter(assetEntity -> symbol.equals(assetEntity.getSymbol())).findFirst();
        }
        return Optional.empty();
    }

    public Optional<AssetHistoryEntity> getAssetHistory(String assetId) {
        Response response = requestAssetHistory(assetId);
        if(response.code() == HttpURLConnection.HTTP_OK) {
            Optional<AssetHistoryData> assetHistoryData = convertAssetHistoryResponseBody(response.body());
            if(assetHistoryData.isPresent() && assetHistoryData.get().getData().size() > 0) {
                return Optional.ofNullable(assetHistoryData.get().getData().get(0));
            }
        }
        return Optional.empty();
    }

    private Response requestAsset(String symbol) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(baseURL + "assets").newBuilder();
        urlBuilder.addQueryParameter("search", symbol);

        return getResponse(urlBuilder);
    }

    private Response requestAssetHistory(String assetId) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(baseURL + "assets").newBuilder();
        urlBuilder.addPathSegment(assetId);
        urlBuilder.addPathSegment("history");
        urlBuilder.addQueryParameter("interval", historyInterval);
        urlBuilder.addQueryParameter("start", historyStart);
        urlBuilder.addQueryParameter("end", historyEnd);

        return getResponse(urlBuilder);
    }

    @NotNull
    private Response getResponse(HttpUrl.Builder urlBuilder) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<AssetData> convertAssetResponseBody(ResponseBody responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            AssetData assetData = objectMapper.readValue(responseBody.string(), AssetData.class);
            return Optional.ofNullable(assetData);
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
