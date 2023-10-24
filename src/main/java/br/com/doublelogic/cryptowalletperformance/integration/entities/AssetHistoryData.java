package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AssetHistoryData {

    @Getter
    @Setter
    private List<AssetHistoryEntity> data;

    @Getter
    @Setter
    private long timestamp;
}
