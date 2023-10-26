package br.com.doublelogic.cryptowalletperformance.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CoincapAPI.class)
public class CoincapAPITest {

    private static final String VALID_SYMBOL = "ETH";
    private static final String INVALID_SYMBOL = "ABS";
    private static final String VALID_ID = "bitcoin";
    private static final String INVALID_ID = "bitcopper";

    @Autowired
    private CoincapAPI coincapAPI;

    @Test
    void testGetAssetWithValidSymbol() {
        var assetEntity = coincapAPI.getAsset(VALID_SYMBOL);

        Assertions.assertTrue(assetEntity.isPresent());
        Assertions.assertEquals("ethereum", assetEntity.get().getId());
        Assertions.assertEquals("ETH", assetEntity.get().getSymbol());
        Assertions.assertEquals("Ethereum", assetEntity.get().getName());
    }

    @Test
    void testGetAssetWithInvalidSymbol() {
        var assetEntity = coincapAPI.getAsset(INVALID_SYMBOL);

        Assertions.assertFalse(assetEntity.isPresent());
    }

    @Test
    void testGetAssetHistoryWithValidId() {
        var assetHistoryEntity = coincapAPI.getAssetHistory(VALID_ID);

        Assertions.assertTrue(assetHistoryEntity.isPresent());
        Assertions.assertEquals(56999.97282520531, assetHistoryEntity.get().getPriceUsd());
    }

    @Test
    void testGetAssetHistoryWithInvalidId() {
        var assetHistoryEntity = coincapAPI.getAssetHistory(INVALID_ID);

        Assertions.assertFalse(assetHistoryEntity.isPresent());
    }

}
