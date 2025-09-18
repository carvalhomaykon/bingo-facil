package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.repositories.card.NumberBingoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberBingoService {

    private final NumberBingoRepository repository;

    public NumberBingoService(NumberBingoRepository repository) {
        this.repository = repository;
    }

    public void populateNumbers(){
        if (repository.count() == 0){
            for (int i = 1; i <= 75; i++){
                String letter;
                if (i <= 15){
                    letter = "B";
                } else if (i <= 30) {
                    letter = "I";
                } else if (i <= 45){
                    letter = "N";
                } else if (i <= 60){
                    letter = "G";
                } else{
                    letter = "O";
                }
                repository.save(new NumberBingo(null, i, letter));
            }
        }
    }

}
