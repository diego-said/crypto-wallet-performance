package br.com.doublelogic.cryptowalletperformance;

import br.com.doublelogic.cryptowalletperformance.core.WalletProcessor;
import br.com.doublelogic.cryptowalletperformance.core.entities.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptoWalletPerformanceApplication {

    @Autowired
    private WalletProcessor walletProcessor;

    public static void main(String[] args) {
        SpringApplication.run(CryptoWalletPerformanceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Wallet wallet = walletProcessor.process();
            System.out.println(wallet);
        };
    }

}
