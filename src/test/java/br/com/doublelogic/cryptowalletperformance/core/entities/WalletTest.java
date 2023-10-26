package br.com.doublelogic.cryptowalletperformance.core.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest(classes = Wallet.class)
public class WalletTest {

    private Wallet wallet;
    private Asset assetBitcoin;
    private Asset assetEthereum;

    @BeforeEach
    void init() {
        wallet = new Wallet();

        assetBitcoin = new Asset();
        assetBitcoin.setId("bitcoin");
        assetBitcoin.setName("Bitcoin");
        assetBitcoin.setSymbol("BTC");
        assetBitcoin.setQuantity(0.12345);
        assetBitcoin.setOriginalPrice(37870.5058);
        assetBitcoin.setPrice(56999.9728252053067291);

        assetEthereum = new Asset();
        assetEthereum.setId("ethereum");
        assetEthereum.setName("Ethereum");
        assetEthereum.setSymbol("ETH");
        assetEthereum.setQuantity(4.89532);
        assetEthereum.setOriginalPrice(2004.9774);
        assetEthereum.setPrice(2032.1394325557042107);
    }

    @Test
    void testAddAsset() {
        Assertions.assertEquals(0, wallet.getAssetListSize());
        wallet.addAsset(assetBitcoin);
        Assertions.assertEquals(1, wallet.getAssetListSize());
    }

    @Test
    void testUpdate() {
        Assertions.assertEquals(0, wallet.getAssetListSize());

        wallet.addAsset(assetBitcoin);
        wallet.addAsset(assetEthereum);

        Assertions.assertEquals(2, wallet.getAssetListSize());

        wallet.update();

        Assertions.assertNotNull(wallet.getBestAsset());
        Assertions.assertEquals(assetBitcoin, wallet.getBestAsset());

        Assertions.assertNotNull(wallet.getWorstAsset());
        Assertions.assertEquals(assetEthereum, wallet.getWorstAsset());

        Assertions.assertEquals(16984.62, wallet.getTotal());
    }

    @Test
    void testUpdateWithOneAsset() {
        Assertions.assertEquals(0, wallet.getAssetListSize());

        wallet.addAsset(assetBitcoin);

        Assertions.assertEquals(1, wallet.getAssetListSize());

        wallet.update();

        Assertions.assertNotNull(wallet.getBestAsset());
        Assertions.assertEquals(assetBitcoin, wallet.getBestAsset());

        Assertions.assertNotNull(wallet.getWorstAsset());
        Assertions.assertEquals(assetBitcoin, wallet.getWorstAsset());

        Double total = BigDecimal
                .valueOf(assetBitcoin.getCurrentPosition())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        Assertions.assertEquals(total, wallet.getTotal());
    }

    @Test
    void testUpdateWithNoAsset() {
        Assertions.assertEquals(0, wallet.getAssetListSize());

        wallet.update();

        Assertions.assertNull(wallet.getBestAsset());
        Assertions.assertNull(wallet.getWorstAsset());

        Assertions.assertEquals(0.00, wallet.getTotal());
    }
}
