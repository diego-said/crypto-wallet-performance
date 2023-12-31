package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

public class CoincapAssetHistory {

    @Getter
    @Setter
    private Double priceUsd;

    @Getter
    @Setter
    private long time;

    @Getter
    @Setter
    private String date;

}
