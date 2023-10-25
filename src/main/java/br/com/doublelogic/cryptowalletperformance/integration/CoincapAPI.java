package br.com.doublelogic.cryptowalletperformance.integration;

import br.com.doublelogic.cryptowalletperformance.integration.entities.CoincapAsset;
import br.com.doublelogic.cryptowalletperformance.integration.entities.CoincapAssetData;
import br.com.doublelogic.cryptowalletperformance.integration.entities.CoincapAssetHistory;
import br.com.doublelogic.cryptowalletperformance.integration.entities.CoincapAssetHistoryData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CoincapAPI {

    private static final Logger logger = LoggerFactory.getLogger(CoincapAPI.class);

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

    public Optional<CoincapAsset> getAsset(String symbol) {
        logger.info("Submitted request to get asset identified by symbol [" + symbol +"]");
        Response response = requestAsset(symbol);
        logger.info("Asset [" + symbol +"] status: " + response.code());
        if(response.code() == HttpURLConnection.HTTP_OK) {
            Optional<CoincapAssetData> assetData = convertAssetResponseBody(response.body());
            if(assetData.isPresent())
                return assetData.get().getData().stream().filter(assetEntity -> symbol.equals(assetEntity.getSymbol())).findFirst();
        }
        return Optional.empty();
    }

    public Optional<CoincapAssetHistory> getAssetHistory(String assetId) {
        logger.info("Submitted request to get price for asset [" + assetId +"]");
        Response response = requestAssetHistory(assetId);
        logger.info("Asset [" + assetId +"] status: " + response.code());
        if(response.code() == HttpURLConnection.HTTP_OK) {
            Optional<CoincapAssetHistoryData> assetHistoryData = convertAssetHistoryResponseBody(response.body());
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
            logger.error("Failed to execute call for url: " + urlBuilder.build(), e);
            throw new RuntimeException(e);
        }
    }

    private Optional<CoincapAssetData> convertAssetResponseBody(ResponseBody responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            CoincapAssetData assetData = objectMapper.readValue(responseBody.string(), CoincapAssetData.class);
            return Optional.ofNullable(assetData);
        } catch (IOException e) {
            logger.error("Failed to convert asset", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<CoincapAssetHistoryData> convertAssetHistoryResponseBody(ResponseBody responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CoincapAssetHistoryData assetHistoryData = objectMapper.readValue(responseBody.string(), CoincapAssetHistoryData.class);
            return Optional.ofNullable(assetHistoryData);
        } catch (IOException e) {
            logger.error("Failed to convert asset history", e);
            throw new RuntimeException(e);
        }
    }

}
