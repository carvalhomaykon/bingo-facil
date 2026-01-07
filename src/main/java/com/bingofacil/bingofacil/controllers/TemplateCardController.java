package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.TemplateCardDTO;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import com.bingofacil.bingofacil.services.card.TemplateCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/template-card")
public class TemplateCardController {

    @Autowired
    private TemplateCardService templateCardService;

    @GetMapping("/{id}")
    public ResponseEntity<TemplateCard> findTemplateCardById(@PathVariable Long id){
        TemplateCard templateCard = templateCardService.findTemplateCardById(id);

        return new ResponseEntity<>(templateCard, HttpStatus.OK);
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<TemplateCard> findTemplateCardByIdProject(@PathVariable Long idProject){
        TemplateCard templateCard = templateCardService.findTemplateCardByProjecId(idProject);

        return new ResponseEntity<>(templateCard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TemplateCard> saveTemplateCard(@RequestBody TemplateCardDTO data){
        TemplateCard templateCardSave = templateCardService.saveTemplateCard(data);

        return new ResponseEntity<>(templateCardSave, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateCard> editTemplateCard(@PathVariable Long id, @RequestBody TemplateCardDTO data){
        TemplateCard templateCard = templateCardService.editTemplateCard(id, data);

        return new ResponseEntity<>(templateCard, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void removeTemplateCard(@PathVariable Long id) {
        templateCardService.deleteTemplateCard(id);
    }

}
