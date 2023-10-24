package br.com.doublelogic.cryptowalletperformance;

import br.com.doublelogic.cryptowalletperformance.integration.CoincapAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class CryptoWalletPerformanceApplication {

	@Autowired
	private CoincapAPI coincapAPI;

	public static void main(String[] args) {
		SpringApplication.run(CryptoWalletPerformanceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println(coincapAPI.getAssetHistory("bitcoin").get().getPriceUsd());
		};
	}

}
