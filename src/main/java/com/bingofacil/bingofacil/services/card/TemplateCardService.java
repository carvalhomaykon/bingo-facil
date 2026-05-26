package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.TemplateCardDTO;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.repositories.card.TemplateCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.services.project.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class TemplateCardService {

    @Autowired
    private TemplateCardRepository templateCardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.endpoint}")
    private String s3Endpoint;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "_");
        return performUpload(file, fileName);
    }

    public String updateFile(MultipartFile file, String oldUrl) throws IOException {
        String fileName = oldUrl.substring(oldUrl.lastIndexOf("/") + 1);
        return performUpload(file, fileName);
    }

    private String performUpload(MultipartFile file, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody
                .fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("%s/%s/%s", s3Endpoint, bucketName, fileName);
    }

    public TemplateCard findTemplateCardById(Long id){
        return templateCardRepository.findById(id).orElse(null);
    }

    public TemplateCard findTemplateCardByProjecId(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project não encontrado.")
        );

        return templateCardRepository.findByProjectId(project.getId());
    }

    public TemplateCard saveTemplateCard(TemplateCardDTO dto){
        Project project = projectRepository.findById(dto.project()).orElseThrow(
                () -> new EntityNotFoundException("Projeto com ID " + dto.project() + " não encontrado.")
        );

        TemplateCard newTemplateCard = new TemplateCard(dto);
        newTemplateCard.setProject(project);

        return templateCardRepository.save(newTemplateCard);
    }

    public TemplateCard editTemplateCard(Long id, TemplateCardDTO dto){
        TemplateCard templateCardEdit = templateCardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("TemplateCard não encontrado")
        );

        templateCardEdit.setLayoutConfig(dto.layoutConfig());
        templateCardEdit.setBackgroundImagePath(dto.backgroundImagePath());

        return templateCardRepository.save(templateCardEdit);
    }

    public void deleteTemplateCard(Long id){
        TemplateCard templateCard = templateCardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("TemplateCard não encontrado")
        );

        templateCardRepository.deleteById(id);
    }

}
