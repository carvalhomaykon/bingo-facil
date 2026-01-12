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
import java.util.UUID;

@RestController
@RequestMapping("/template-card")
public class TemplateCardController {

    @Autowired
    private TemplateCardService templateCardService;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.endpoint}")
    private String s3Endpoint;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadToCloud(@RequestParam("file")MultipartFile file){
        try{
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.
                            fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = String.format("%s/%s/%s", s3Endpoint, bucketName, fileName);

            return ResponseEntity.ok(Map.of("url", fileUrl));

        } catch (IOException e){
            return ResponseEntity.status(500).body(Map.of("error", "Falha no upload"));
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
