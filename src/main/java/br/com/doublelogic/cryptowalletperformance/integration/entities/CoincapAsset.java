package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

public class CoincapAsset {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private String name;

    @Override
    public String toString() {
        return "AssetEntity{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
