package br.com.doublelogic.cryptowalletperformance.integration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CoincapAssetHistoryData {

    @Getter
    @Setter
    private List<CoincapAssetHistory> data;

    @Getter
    @Setter
    private long timestamp;

}
