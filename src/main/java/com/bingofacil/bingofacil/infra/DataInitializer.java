package com.bingofacil.bingofacil.infra;

import com.bingofacil.bingofacil.services.card.NumberBingoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(NumberBingoService numberBingoService){
        return args -> numberBingoService.populateNumbers();
    }

}
