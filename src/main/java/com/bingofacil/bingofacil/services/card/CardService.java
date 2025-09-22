package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.card.CardRepository;
import com.bingofacil.bingofacil.repositories.card.NumberBingoRepository;
import com.bingofacil.bingofacil.repositories.card.NumberCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private NumberCardRepository numberCardRepository;

    @Autowired
    private NumberCardService numberCardService;

    // Pegar card pelo id
    public Card findCardById(Long id){
        return cardRepository.findById(id).orElse(null);
    }

    // Pegar todos os cards pelo id do usuário
    public List<Card> findCardsByIduser(Long userId){
        return cardRepository.findAllByUserId(userId);
    }

    // Pegar todos os cards pelo id do project
    public List<Card> findCardsByIdProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project não encontrado.")
        );

        return cardRepository.findAllByProjectId(project.getId());
    }

    public Card createCard(CardDTO cardRequest){
        User user = userRepository.findById(cardRequest.user()).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado.")
        );

        Project project = projectRepository.findById(cardRequest.project()).orElseThrow(
                () -> new RuntimeException("Projeto não encontrado.")
        );

        Card newCard = new Card();
        newCard.setUser(user);
        newCard.setProject(project);

        newCard = cardRepository.save(newCard);

        numberCardService.createNumberCards(newCard);

        return newCard;

    }

}
