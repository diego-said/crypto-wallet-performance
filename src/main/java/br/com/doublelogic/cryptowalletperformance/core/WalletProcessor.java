package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Asset;
import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WalletProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WalletProcessor.class);

    @Value(value = "${crypto.wallet.asset.buffer.size}")
    private int bufferSize;

    @Value(value = "${crypto.wallet.asset.processors}")
    private int assetProcessors;

    @Autowired
    private WalletReader walletReader;

    @Autowired
    private CoincapAPI coincapAPI;

    public Wallet process() {
        logger.info("Starting - processing wallet");
        final var wallet = walletReader.read();
        if (!wallet.getAssetList().isEmpty() && assetProcessors > 0) {
            final List<Asset> assetListToBeRemoved = Collections.synchronizedList(new ArrayList<>(wallet.getAssetListSize()));
            final BlockingQueue<Asset> queue = new ArrayBlockingQueue<>(bufferSize);
            final AtomicInteger processorCounter = new AtomicInteger();

            createAssetProcessors(queue, processorCounter, assetListToBeRemoved);

            for(Asset asset : wallet.getAssetList()) {
                try {
                    queue.put(asset);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            destroyAssetProcessors(queue);

            while (processorCounter.get() != assetProcessors) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            wallet.getAssetList().removeAll(assetListToBeRemoved);
            wallet.update();
        }
        logger.info("Finished - processing wallet");
        return wallet;
    }

    private void createAssetProcessors(BlockingQueue<Asset> queue, AtomicInteger processorCounter, List<Asset> assetListToBeRemoved) {
        for(int i = 0; i < assetProcessors; i++) {
            AssetProcessor assetProcessor = new AssetProcessor(queue, processorCounter, coincapAPI, assetListToBeRemoved);
            new Thread(assetProcessor).start();
        }
    }

    private void destroyAssetProcessors(BlockingQueue<Asset> queue) {
        for(int i = 0; i < assetProcessors; i++) {
            Asset poisonAsset = new Asset();
            poisonAsset.setSymbol(AssetProcessor.POISON_SYMBOL);

            try {
                queue.put(poisonAsset);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
