package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Asset;
import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.io.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletReader {

    private static final Logger logger = LoggerFactory.getLogger(WalletReader.class);

    private static final int CSV_JUST_WITH_HEADER_VALUE = 1;

    private static final int SYMBOL_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;
    private static final int PRICE_INDEX = 2;

    @Autowired
    private CSVReader csvReader;

    public Wallet read() {
        final Wallet wallet = new Wallet();
        logger.info("Starting - reading wallet");
        var records = csvReader.readRecords();
        if(records.size() > CSV_JUST_WITH_HEADER_VALUE) {
            records.stream().skip(1).forEach(columns -> {
                Asset asset = new Asset();
                asset.setSymbol(columns.get(SYMBOL_INDEX));
                asset.setQuantity(Double.valueOf(columns.get(QUANTITY_INDEX)));
                asset.setOriginalPrice(Double.valueOf(columns.get(PRICE_INDEX)));
                wallet.addAsset(asset);
                logger.info("Asset found [" + asset + "]");
            });
        }
        logger.info("Finished - reading wallet");
        return wallet;
    }

}
