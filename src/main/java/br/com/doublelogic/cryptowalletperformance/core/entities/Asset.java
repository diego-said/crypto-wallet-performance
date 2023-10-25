package br.com.doublelogic.cryptowalletperformance.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Asset {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Double quantity;

    @Getter
    @Setter
    private Double originalPrice;

    @Getter
    @Setter
    private Double price;

    private Double currentPosition;

    private Double performance;

    public Double getCurrentPosition() {
        if(currentPosition == null) {
            BigDecimal bigQuantity = BigDecimal.valueOf(quantity);
            BigDecimal bigPrice = BigDecimal.valueOf(price);
            currentPosition = bigQuantity.multiply(bigPrice).doubleValue();
        }
        return currentPosition;
    }

    public Double getPerformance() {
        if(performance == null) {
            BigDecimal bigOriginalPrice = BigDecimal.valueOf(originalPrice);
            BigDecimal bigPrice = BigDecimal.valueOf(price);
            performance = bigPrice.divide(bigOriginalPrice, 2, RoundingMode.HALF_UP).doubleValue();
        }
        return performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(symbol, asset.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
