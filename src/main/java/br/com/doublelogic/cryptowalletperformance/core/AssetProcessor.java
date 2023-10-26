package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Asset;
import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AssetProcessor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AssetProcessor.class);

    public static final String POISON_SYMBOL = "vodka";

    private final BlockingQueue<Asset> queue;

    private final AtomicInteger processorCounter;

    private final CoincapAPI coincapAPI;

    private final List<Asset> assetListToBeRemoved;

    public AssetProcessor(BlockingQueue<Asset> queue, AtomicInteger processorCounter, CoincapAPI coincapAPI, List<Asset> assetListToBeRemoved) {
        this.queue = queue;
        this.processorCounter = processorCounter;
        this.coincapAPI = coincapAPI;
        this.assetListToBeRemoved = assetListToBeRemoved;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Asset asset = queue.take();
                if (POISON_SYMBOL.equals(asset.getSymbol())) {
                    processorCounter.getAndDecrement();
                    break;
                }

                var coincapAsset = coincapAPI.getAsset(asset.getSymbol());
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
            }
        } catch (InterruptedException e) {
            logger.error("AssetProcessor - ", e);
            Thread.currentThread().interrupt();
        }
    }
}
