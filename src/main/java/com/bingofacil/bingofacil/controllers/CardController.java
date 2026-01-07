package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.project.StyleCard;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
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
    public ResponseEntity<?> generateCard(
            @PathVariable int amount,
            @RequestBody CardDTO requestCard,
            @PathVariable StyleCard typeCards) throws IOException {
        byte[] bytes = cardService.generateCardsPDF(amount, requestCard, typeCards);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping()
    public ResponseEntity<List<Card>> findAllCards(Principal principal){
        List<Card> cards = cardService.findAllCards(principal);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/{idCard}")
    public ResponseEntity<Card> findCardById(@PathVariable Long idCard){
        Card card = cardService.findCardById(idCard);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<List<Card>> findCardsByIdProject(@PathVariable Long idProject){
        List<Card> cards = cardService.findCardsByIdProject(idProject);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/{codeCard}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByCodeCard(@PathVariable String codeCard){
        List<NumberCard> numberCards = numberCardService.findNumberCardByCodeCard(codeCard);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }

    @GetMapping("/project/{idProject}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByIdProject(@PathVariable Long idProject){
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdProject(idProject);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }

    @GetMapping("/code-card/{codeCard}")
    public ResponseEntity<Card> findCardByCodeCard(@PathVariable String codeCard, Principal principal){
        Card card = cardService.findCardByCodeCard(codeCard, principal);

        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/user/{idUser}/numbers-card")
    public ResponseEntity<List<NumberCard>> findNumberCardByIdUser(@PathVariable Long idUser){
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdProject(idUser);
        return new ResponseEntity<>(numberCards, HttpStatus.OK);
    }
}
