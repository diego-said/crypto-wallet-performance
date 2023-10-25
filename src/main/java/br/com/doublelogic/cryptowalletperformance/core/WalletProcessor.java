package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WalletProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WalletProcessor.class);

    @Autowired
    private WalletReader walletReader;

    @Autowired
    private CoincapAPI coincapAPI;

    public Wallet process() {
        logger.info("Starting - processing wallet");
        var wallet = walletReader.read();
        final var assetListToBeRemoved = new ArrayList<>(wallet.getAssetListSize());
        if (!wallet.getAssetList().isEmpty()) {
            wallet.getAssetList().stream().forEach(asset -> {
                var coincapAsset =  coincapAPI.getAsset(asset.getSymbol());
                if (coincapAsset.isPresent()) {
                    asset.setId(coincapAsset.get().getId());
                    asset.setName(coincapAsset.get().getName());

                    var coincapAssetHistory = coincapAPI.getAssetHistory(asset.getId());
                    if (coincapAssetHistory.isPresent()) {
                        asset.setPrice(coincapAssetHistory.get().getPriceUsd());
                    } else {
                        logger.info("Asset price not found [" + asset + "]");
                        assetListToBeRemoved.add(asset);
                    }
                } else {
                    logger.info("Asset not found [" + asset + "]");
                    assetListToBeRemoved.add(asset);
                }
            });
            wallet.update();
        }
        logger.info("Finished - processing wallet");
        return wallet;
    }

}
