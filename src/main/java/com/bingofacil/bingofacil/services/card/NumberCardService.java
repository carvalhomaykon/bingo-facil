package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.card.CardRepository;
import com.bingofacil.bingofacil.repositories.card.NumberBingoRepository;
import com.bingofacil.bingofacil.repositories.card.NumberCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.award.AwardService;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class NumberCardService {

    @Autowired
    private NumberCardRepository numberCardRepository;

    @Autowired
    private NumberBingoRepository numberBingoRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<NumberCard> createNumberCards(Card card){
        List<NumberCard> numbersCard = new ArrayList<>();

        Random random = new Random();

        int[][] ranges = {
                {1, 15},
                {16, 30},
                {31, 45},
                {46, 60},
                {61, 75}
        };

        for (int col = 0; col < 5; col++){
            int start = ranges[col][0];
            int end = ranges[col][1];

            // Sorteia números únicos dentro da coluna
            List<Integer> nums = random.ints(start, end + 1)
                    .distinct()
                    .limit(5) // A coluna N tem somente 4 números
                    .boxed()
                    .toList();

            for (int row = 0; row < 5; row++){
                NumberBingo nb = numberBingoRepository
                        .findByValueAndLetter(nums.get(row), getLetter(col))
                        .orElseThrow(() -> new RuntimeException("Número não encontrado no banco"));
                NumberCard nc = new NumberCard();
                nc.setCard(card);
                nc.setNumber(nb);
                nc.setRow(row);
                nc.setColumn(col);
                nc.setMarked(false);
                numbersCard.add(numberCardRepository.save(nc));
            }
        }

        // Centro da cartela = FREE
        NumberCard free = new NumberCard();
        free.setCard(card);
        free.setNumber(null); // ou pode criar um NumberBingo especial "FREE"
        free.setRow(2);
        free.setColumn(2);
        free.setMarked(true); // já marcado
        numbersCard.add(numberCardRepository.save(free));

        return numbersCard;
    }

    private String getLetter(int col) {
        return switch (col) {
            case 0 -> "B";
            case 1 -> "I";
            case 2 -> "N";
            case 3 -> "G";
            case 4 -> "O";
            default -> throw new IllegalArgumentException("Coluna inválida");
        };
    }

    public List<NumberCard> findNumberCardByIdCard(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new RuntimeException("Card não encontrado.")
        );

        return numberCardRepository.findAllByCardId(card.getId());
    }

    public List<NumberCard> findNumberCardByCodeCard(String codeCard){
        Card card = cardRepository.findByCodeCard(codeCard).orElseThrow(
                () -> new RuntimeException("Card não encontrado. Revise o code card informado.")
        );

        return numberCardRepository.findAllByCardId(card.getId());
    }

    public List<NumberCard> findNumberCardByIdProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project not found")
        );

        return numberCardRepository.findAllByIdProject(project.getId());
    }

}
