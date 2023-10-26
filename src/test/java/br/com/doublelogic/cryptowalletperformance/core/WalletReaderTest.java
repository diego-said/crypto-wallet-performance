package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.io.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CSVReader.class, WalletReader.class})
public class WalletReaderTest {

    @Autowired
    private CSVReader csvReader;

    @Autowired
    private WalletReader walletReader;

    @BeforeEach
    void init() {
        csvReader.setFileName("wallet.csv");
    }

    @Test
    void testRead() {
        Wallet wallet = walletReader.read();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(2, wallet.getAssetListSize());
    }

    @Test
    void testReadWithEmptyFile() {
        csvReader.setFileName("wallet_empty.csv");

        Wallet wallet = walletReader.read();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(0, wallet.getAssetListSize());
    }

    @Test
    void testReadWithJustHeader() {
        csvReader.setFileName("wallet_header.csv");

        Wallet wallet = walletReader.read();

        Assertions.assertNotNull(wallet);
        Assertions.assertEquals(0, wallet.getAssetListSize());
    }

    @Test
    void testReadWithInvalidFileName() {
        csvReader.setFileName("not_a_csv_file");

        Assertions.assertThrows(RuntimeException.class, () -> walletReader.read());
    }

}
