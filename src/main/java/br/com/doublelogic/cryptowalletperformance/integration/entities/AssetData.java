package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AssetData {

    @Getter
    @Setter
    private List<AssetEntity> data;

    @Getter
    @Setter
    private long timestamp;

}
