package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.services.card.CardService;
import com.bingofacil.bingofacil.services.card.NumberCardService;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private NumberCardService numberCardService;

    @Autowired
    private CardService cardService;

    @PostMapping("/{amount}/{typeCards}")
    public ResponseEntity<?> generateCard(@PathVariable int amount, @RequestBody CardDTO requestCard, @PathVariable String typeCards){
        try{
            byte[] bytes = cardService.generateCardsPDF(amount, requestCard, typeCards);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bytes);

        } catch (Exception e) {
            e.printStackTrace();

            String errorMessage = "Erro ao gerar PDF: " + e.getMessage();

            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMessage);
        }
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
