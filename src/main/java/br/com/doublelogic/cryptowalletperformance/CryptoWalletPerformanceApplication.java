package br.com.doublelogic.cryptowalletperformance;

import br.com.doublelogic.cryptowalletperformance.io.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptoWalletPerformanceApplication {

	@Autowired
	private CSVReader csvReader;

	public static void main(String[] args) {
		SpringApplication.run(CryptoWalletPerformanceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			var records = csvReader.readRecords();

			records.stream().forEach(columns -> {
				if (columns.size() == 3) {
					System.out.println("[" + columns.get(0) + "]");
					System.out.println("[" + columns.get(1) + "]");
					System.out.println("[" + columns.get(2) + "]");
				}
			});
		};
	}

}
