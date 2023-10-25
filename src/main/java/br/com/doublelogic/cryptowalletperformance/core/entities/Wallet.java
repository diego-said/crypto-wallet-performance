package br.com.doublelogic.cryptowalletperformance.core.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private static final String DEFAULT_ASSET_SYMBOL = "AST";
    private static final double DEFAULT_DOUBLE_VALUE = 0.0;

    @Getter
    private Double total;

    @Getter
    private Asset bestAsset;

    @Getter
    private Asset worstAsset;

    @Getter
    private List<Asset> assetList = new ArrayList<>();

    public void addAsset(Asset asset) {
        assetList.add(asset);
    }

    public int getAssetListSize() {
        return assetList.size();
    }

    public void update() {
        BigDecimal bigTotal = new BigDecimal(0.0);
        for (Asset asset : assetList) {
            bigTotal = bigTotal.add(BigDecimal.valueOf(asset.getCurrentPosition()));
            findBestAsset(asset);
            findWorstAsset(asset);
        }
        total = bigTotal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private void findBestAsset(Asset asset) {
        if(bestAsset != null) {
            if(Double.compare(bestAsset.getPerformance(), asset.getPerformance()) <= 0) {
                bestAsset = asset;
            }
        } else {
            bestAsset = asset;
        }
    }

    private void findWorstAsset(Asset asset) {
        if(worstAsset != null) {
            if(Double.compare(worstAsset.getPerformance(), asset.getPerformance()) >= 0) {
                worstAsset = asset;
            }
        } else {
            worstAsset = asset;
        }
    }

    @Override
    public String toString() {
        return String.format("total=%.2f,best_asset=%s,best_performance=%.2f,worst_asset=%s,worst_performance=%.2f",
                total != null ? total : DEFAULT_DOUBLE_VALUE,
                bestAsset != null ? bestAsset.getSymbol() : DEFAULT_ASSET_SYMBOL,
                bestAsset != null ? bestAsset.getPerformance() : DEFAULT_DOUBLE_VALUE,
                worstAsset != null ? worstAsset.getSymbol() : DEFAULT_ASSET_SYMBOL,
                worstAsset != null ? worstAsset.getPerformance() : DEFAULT_DOUBLE_VALUE);
    }
}
