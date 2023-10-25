package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CoincapAssetData {

    @Getter
    @Setter
    private List<CoincapAsset> data;

    @Getter
    @Setter
    private long timestamp;

}
