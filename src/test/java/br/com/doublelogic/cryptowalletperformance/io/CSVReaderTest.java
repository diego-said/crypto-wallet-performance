package br.com.doublelogic.cryptowalletperformance.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CSVReaderTest {

    @Autowired
    private CSVReader reader;

    @BeforeEach
    void init() {
        reader.setFileName("wallet.csv");
    }

    @Test
    void testReadRecords() {
        var records = reader.readRecords();

        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(3, records.size());

        // test header
        List<String> headerColumns = reader.readRecords().get(0);
        Assertions.assertEquals("symbol", headerColumns.get(0));
        Assertions.assertEquals("quantity", headerColumns.get(1));
        Assertions.assertEquals("price", headerColumns.get(2));

        // test bitcoin asset
        List<String> bitcoinColumns = reader.readRecords().get(1);
        Assertions.assertEquals("BTC", bitcoinColumns.get(0));
        Assertions.assertEquals("0.12345", bitcoinColumns.get(1));
        Assertions.assertEquals("37870.5058", bitcoinColumns.get(2));

        // test ethereum asset
        List<String> ethereumColumns = reader.readRecords().get(2);
        Assertions.assertEquals("ETH", ethereumColumns.get(0));
        Assertions.assertEquals("4.89532", ethereumColumns.get(1));
        Assertions.assertEquals("2004.9774", ethereumColumns.get(2));
    }

    @Test
    void testReadRecordsWithEmptyFile() {
        reader.setFileName("wallet_empty.csv");
        var records = reader.readRecords();

        Assertions.assertTrue(records.isEmpty());
    }

    @Test
    void testReadRecordsWithJustHeader() {
        reader.setFileName("wallet_header.csv");
        var records = reader.readRecords();

        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(1, records.size());

        // test header
        List<String> headerColumns = reader.readRecords().get(0);
        Assertions.assertEquals("symbol", headerColumns.get(0));
        Assertions.assertEquals("quantity", headerColumns.get(1));
        Assertions.assertEquals("price", headerColumns.get(2));
    }

    @Test
    void testReadRecordsWithInvalidFileName() {
        reader.setFileName("not_a_csv_file");
        Assertions.assertThrows(RuntimeException.class, () -> {
            reader.readRecords();
        });
    }

}
