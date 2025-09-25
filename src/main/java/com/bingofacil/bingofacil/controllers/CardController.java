package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.services.card.CardService;
import com.bingofacil.bingofacil.services.card.NumberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private NumberCardService numberCardService;

    @Autowired
    private CardService cardService;

    @PostMapping("/{amount}")
    public ResponseEntity<List<Card>> generateCard(@PathVariable int amount, @RequestBody CardDTO requestCard){
        List<Card> card = cardService.generateCards(amount, requestCard);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    // Pegar card pelo id
    @GetMapping("/{idCard}")
    public ResponseEntity<Card> findCardById(@PathVariable Long idCard){
        Card card = cardService.findCardById(idCard);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    // Buscar card pelo usuário
    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Card>> findCardsByIdUser(@PathVariable Long idUser){
        List<Card> cards = cardService.findCardsByIdUser(idUser);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    // Buscar card pelo usuário
    @GetMapping("/project/{idProject}")
    public ResponseEntity<List<Card>> findCardsByIdProject(@PathVariable Long idProject){
        List<Card> cards = cardService.findCardsByIdProject(idProject);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    // Pegar cartela pelo id da cartela
    @GetMapping("/{idCard}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByIdCard(@PathVariable Long idCard){
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdCard(idCard);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }

    // Get cards by id projeto
    @GetMapping("/project/{idProject}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByIdProject(@PathVariable Long idProject){
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdProject(idProject);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }

    // Get cards by id projeto
    @GetMapping("/user/{idUser}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByIdUser(@PathVariable Long idUser){
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdProject(idUser);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }
}
