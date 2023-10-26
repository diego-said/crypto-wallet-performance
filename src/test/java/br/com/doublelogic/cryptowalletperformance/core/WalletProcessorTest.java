package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Asset;
import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import br.com.doublelogic.cryptowalletperformance.io.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {WalletProcessor.class, CSVReader.class, WalletReader.class, CoincapAPI.class})
public class WalletProcessorTest {

    @Autowired
    private CSVReader csvReader;

    @Autowired
    private WalletProcessor walletProcessor;

    private Asset assetBitcoin;
    private Asset assetEthereum;

    @BeforeEach
    void init() {
        csvReader.setFileName("wallet.csv");

        assetBitcoin = new Asset();
        assetBitcoin.setId("bitcoin");
        assetBitcoin.setName("Bitcoin");
        assetBitcoin.setSymbol("BTC");

        assetEthereum = new Asset();
        assetEthereum.setId("ethereum");
        assetEthereum.setName("Ethereum");
        assetEthereum.setSymbol("ETH");
    }

    @Test
    void testRead() {
        Wallet wallet = walletProcessor.process();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(2, wallet.getAssetListSize());

        Assertions.assertEquals(assetBitcoin.getSymbol(), wallet.getBestAsset().getSymbol());
        Assertions.assertEquals(assetBitcoin.getId(), wallet.getBestAsset().getId());
        Assertions.assertNotNull(wallet.getBestAsset().getPrice());

        Assertions.assertEquals(assetEthereum.getSymbol(), wallet.getWorstAsset().getSymbol());
        Assertions.assertEquals(assetEthereum.getId(), wallet.getWorstAsset().getId());
        Assertions.assertNotNull(wallet.getWorstAsset().getPrice());
    }

    @Test
    void testReadWithEmptyFile() {
        csvReader.setFileName("wallet_empty.csv");

        Wallet wallet = walletProcessor.process();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(0, wallet.getAssetListSize());
    }

    @Test
    void testReadWithJustHeader() {
        csvReader.setFileName("wallet_header.csv");

        Wallet wallet = walletProcessor.process();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(0, wallet.getAssetListSize());
    }

    @Test
    void testReadWithInvalidFileName() {
        csvReader.setFileName("not_a_csv_file");

        Assertions.assertThrows(RuntimeException.class, () -> walletProcessor.process());
    }

}
