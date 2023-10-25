package br.com.doublelogic.cryptowalletperformance.core.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetTest {

    private Asset asset;

    @BeforeEach
    void init() {
        asset = new Asset();
        asset.setId("ethereum");
        asset.setName("Ethereum");
        asset.setSymbol("ETH");
        asset.setQuantity(4.89532);
        asset.setOriginalPrice(2004.9774);
        asset.setPrice(2032.1394325557042107);
    }

    @Test
    void testGetCurrentPosition() {
        Assertions.assertEquals(9947.97280697859, asset.getCurrentPosition());
    }

    @Test
    void testGetPerformance() {
        Assertions.assertEquals(1.01, asset.getPerformance());
    }

}
