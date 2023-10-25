package br.com.doublelogic.cryptowalletperformance.io;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CSVReader {

    private static final Logger logger = LoggerFactory.getLogger(CSVReader.class);

    private static final String COMMA_DELIMITER = ",";

    @Value(value = "${crypto.wallet.csv.name}")
    @Setter
    private String fileName;

    public List<List<String>> readRecords() {
        logger.info("Reading records from file [" + fileName + "]");
        List<List<String>> records = new ArrayList<>();
        ClassPathResource csvResource = new ClassPathResource(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(csvResource.getFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
            logger.info("Total records read: " + records.size());
            return records;
        } catch (FileNotFoundException e) {
            logger.error("File not found [" + fileName + "]", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IO error when trying to read file [" + fileName + "]", e);
            throw new RuntimeException(e);
        }
    }

}
