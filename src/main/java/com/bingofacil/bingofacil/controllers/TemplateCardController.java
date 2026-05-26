package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.TemplateCardDTO;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import com.bingofacil.bingofacil.services.card.TemplateCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/template-card")
public class TemplateCardController {

    @Autowired
    private TemplateCardService templateCardService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file")MultipartFile file){
        try {
            String url = templateCardService.uploadFile(file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Falha no upload"));
        }
    }

    @PutMapping("/update-file")
    public ResponseEntity<Map<String, String>> update(@RequestParam("file") MultipartFile file, @RequestParam("oldUrl") String oldUrl) {
        try {
            String url = templateCardService.updateFile(file, oldUrl);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Falha na atualização"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateCard> findTemplateCardById(@PathVariable Long id){
        TemplateCard templateCard = templateCardService.findTemplateCardById(id);

        return new ResponseEntity<>(templateCard, HttpStatus.OK);
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<TemplateCard> findTemplateCardByIdProject(@PathVariable Long idProject){
        TemplateCard templateCard = templateCardService.findTemplateCardByProjecId(idProject);

        if (templateCard == null) {
            return ResponseEntity.notFound().build();
        }

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
