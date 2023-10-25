package br.com.doublelogic.cryptowalletperformance.core;

import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletProcessor {

    @Autowired
    private WalletReader walletReader;

    @Autowired
    private CoincapAPI coincapAPI;

    public Wallet process() {
        var wallet = walletReader.read();
        if (!wallet.getAssetList().isEmpty()) {
            wallet.getAssetList().stream().forEach(asset -> {
                var coincapAsset =  coincapAPI.getAsset(asset.getSymbol());
                if (coincapAsset.isPresent()) {
                    asset.setId(coincapAsset.get().getId());
                    asset.setName(coincapAsset.get().getName());

                    var coincapAssetHistory = coincapAPI.getAssetHistory(asset.getId());
                    if (coincapAssetHistory.isPresent()) {
                        asset.setPrice(coincapAssetHistory.get().getPriceUsd());
                    }
                }
            });
            wallet.update();
        }
        return wallet;
    }

}
