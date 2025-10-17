package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.services.card.CardService;
import com.bingofacil.bingofacil.services.card.NumberCardService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private NumberCardService numberCardService;

    @Autowired
    private CardService cardService;

    @PostMapping("/{amount}")
    public ResponseEntity<byte[]> generateCard(@PathVariable int amount, @RequestBody CardDTO requestCard){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("Cartela de Bingo", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            List<Card> cards = cardService.generateCards(amount, requestCard);
            int i = 0;
            for (Card card : cards){
                List<NumberCard> numberCards = numberCardService.findNumberCardByIdCard(card.getId());
                for (NumberCard numberCard : numberCards){

                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

        List<Card> cards = cardService.generateCards(amount, requestCard);
        return new ResponseEntity<>(cards, HttpStatus.CREATED);
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
