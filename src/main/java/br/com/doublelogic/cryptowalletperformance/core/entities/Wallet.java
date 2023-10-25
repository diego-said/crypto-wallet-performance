package br.com.doublelogic.cryptowalletperformance.core.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Wallet {

    @Getter
    private Double total;

    @Getter
    private Asset bestAsset;

    @Getter
    private Asset worstAsset;

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

}
