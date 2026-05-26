package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.exception.custom.InvalidAwardAmountException;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.project.StyleCard;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.card.CardRepository;
import com.bingofacil.bingofacil.repositories.card.NumberCardRepository;
import com.bingofacil.bingofacil.repositories.card.TemplateCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.award.AwardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class CardService {

    private static final String PAGE_BREAK = "<br style=\"page-break-before: always;\">";
    private static final String TEMPLATE_PATH = "src/main/resources/templates/cardsBingo.html";
    private static final String PLACEHOLDER_NUMBER_CARD = "{{NUMBER_CARD}}";
    private static final String PLACEHOLDER_NAME_PROJECT = "{{NAME_PROJECT}}";
    private static final String PLACEHOLDER_DATA_PROJECT = "{{DATA_PROJECT}}";
    private static final String PLACEHOLDER_TIME_PROJECT = "{{TIME_PROJECT}}";
    private static final String PLACEHOLDER_VALUE_PROJECT = "{{VALUE_PROJECT}}";
    private static final String PLACEHOLDER_CODE_CARD = "{{CODE_CARD}}";

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

    @Autowired
    private AwardService awardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TemplateCardRepository templateCardRepository;

    public Card findCardById(Long id){
        return cardRepository.findById(id).orElse(null);
    }

    public List<Card> findAllCards(Principal principal){
        String emailLogado = principal.getName();

        User user = userRepository.findByEmail(emailLogado).orElseThrow(
                () -> new RuntimeException("Email não encontrado")
        );

        return cardRepository.findAllByUserId(user.getId());
    }

    public List<Card> findCardsByIdProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project não encontrado.")
        );

        return cardRepository.findAllByProjectId(project.getId());
    }

    public Card findCardByCodeCard(String codeCard, Principal principal){
        Card card = cardRepository.findByCodeCard(codeCard).orElseThrow(
                () -> new RuntimeException("Card não encontrado. Revise o code card informado.")
        );

        String emailLogado = principal.getName();

        User user = userRepository.findByEmail(emailLogado).orElseThrow(
                () -> new RuntimeException("Email não encontrado")
        );

        card.setUser(user);
        cardRepository.save(card);

        return card;
    }

    public byte[] generateCardsPDF(int amountCards, CardDTO requestCard, StyleCard typeCards) throws IOException {
        Project projectCard = projectRepository.findById(requestCard.project()).orElseThrow(
                () -> new EntityNotFoundException("Projeto com ID " + requestCard.project() + " não encontrado.")
        );

        LocalDateTime dateTime = projectCard.getDateAndTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

        String dataProject = dateTime.format(dateFormatter);
        String timeProject = dateTime.format(timeFormatter);

        String nameProject = projectCard.getName();
        String valueProject = currencyFormat.format(projectCard.getValue());

        List<Award> awardsProject = awardService.findByProjectId(projectCard.getId());

        if (!(projectCard.getAmountAwards() == awardsProject.size())) {
            throw new InvalidAwardAmountException("Quantidade de prêmios incompatível com o projeto.");
        }

        List<byte[]> individualCardPdfs = new ArrayList<>();

        for (int i = 0; i < amountCards; i++){
            byte[] cardPdfBytes = createCardPDF(
                    requestCard, typeCards, awardsProject,
                    nameProject, dataProject, timeProject, valueProject
            );
            individualCardPdfs.add(cardPdfBytes);
        }

        return mergePdfs(individualCardPdfs);
    }

    public byte[] createCardPDF(CardDTO cardRequest, StyleCard typeCards, List<Award> awardsProject,
                                String nameProject, String dataProject, String timeProject, String valueProject) throws IOException {
        Card card = generateCard(cardRequest);
        Project project = card.getProject();

        String layoutConfigJson = "[]";
        String backgroundImagePath = "";

        try {
            TemplateCard template = templateCardRepository.findByProjectId(project.getId());
            layoutConfigJson = template.getLayoutConfig();
            backgroundImagePath = template.getBackgroundImagePath();
        } catch (Exception e) {
            layoutConfigJson = "[]";
        }

        List<int[][]> matrizesPresentInPDF = createMatrizesPresentInCard(card);

        StringBuilder pageHtmlBuilder = new StringBuilder();
        pageHtmlBuilder.append("<div class='page-container'>");

        if (backgroundImagePath != null && !backgroundImagePath.isEmpty()) {
            pageHtmlBuilder.append("<img class='background-image' src='").append(backgroundImagePath).append("' />");
        }

        pageHtmlBuilder.append("<div class='canvas-overlay'>");

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode matrizes = mapper.readTree(layoutConfigJson);

            int cardIndex = 0;

            for (JsonNode matriz : matrizes) {
                int[][] matrizCard = matrizesPresentInPDF.size() > cardIndex
                        ? matrizesPresentInPDF.get(cardIndex)
                        : matrizesPresentInPDF.get(0);

                // Coordenadas e tamanhos do Bounding Box exportado pelo front-end
                double x = matriz.get("x").asDouble();
                double y = matriz.get("y").asDouble();
                double width = matriz.get("width").asDouble();

                // Escalas vindas do Fabric.js (Fallback para 1.0 se não existirem)
                double scaleX = matriz.has("scaleX") ? matriz.get("scaleX").asDouble() : 1.0;
                double scaleY = matriz.has("scaleY") ? matriz.get("scaleY").asDouble() : 1.0;

                // Recriando as dimensões exatas do Front-End multiplicadas pela escala
                double tableWidth = 250 * scaleX;
                double tableHeight = 300 * scaleY;
                double cellHeight = 50 * scaleY;

                double headerFontSize = 22 * scaleX;
                double cellFontSize = 22 * scaleX;
                double starFontSize = 26 * scaleX;
                double awardFontSize = 14 * scaleX;

                JsonNode style = matriz.get("styleConfig");

                String rectFill = style.get("rectFill").asText();
                String rectStroke = style.get("rectStroke").asText();
                String textColor = style.get("textColor").asText();
                String fontFamily = style.get("fontFamily").asText();
                String headerFill = style.get("headerFill").asText();
                String headerTextColor = style.get("headerTextColor").asText();
                String borderColor = style.get("borderColor").asText();

                // Wrapper que engloba a Tabela + Textos
                pageHtmlBuilder.append("<div class='card' style='")
                        .append("position: absolute; ")
                        .append("left: ").append(x).append("px; ")
                        .append("top: ").append(y).append("px; ")
                        .append("width: ").append(width).append("px; ") // Usa a largura total da caixa
                        .append("font-family: ").append(fontFamily).append(";'>");

                // Tabela centralizada (margin: 0 auto) dentro do wrapper
                pageHtmlBuilder.append("<table class='bingo-card' style='")
                        .append("margin: 0 auto; ")
                        .append("width: ").append(tableWidth).append("px; ")
                        .append("height: ").append(tableHeight).append("px; ")
                        .append("border: 2px solid ").append(borderColor).append("; ")
                        .append("table-layout: fixed; ") // Força as colunas a terem larguras iguais
                        .append("'>");

                // Cabeçalho B-I-N-G-O
                pageHtmlBuilder.append("<tr class='bingo-header' style='")
                        .append("background-color: ").append(headerFill).append("; ")
                        .append("color: ").append(headerTextColor).append("; ")
                        .append("height: ").append(cellHeight).append("px; ")
                        .append("font-size: ").append(headerFontSize).append("px;")
                        .append("'>");

                String[] letras = {"B", "I", "N", "G", "O"};
                for (String letra : letras) {
                    pageHtmlBuilder
                            .append("<td style='border: 1.5px solid ")
                            .append(rectStroke).append("; text-align: center; vertical-align: middle;'>")
                            .append(letra).append("</td>");
                }
                pageHtmlBuilder.append("</tr>");

                // Linhas de Números
                for (int r = 0; r < 5; r++) {
                    pageHtmlBuilder
                            .append("<tr style='background-color: ")
                            .append(rectFill).append("; color: ").append(textColor).append("; ")
                            .append("height: ").append(cellHeight).append("px; ")
                            .append("font-size: ").append(cellFontSize).append("px;")
                            .append("'>");

                    for (int c = 0; c < 5; c++) {
                        pageHtmlBuilder
                                .append("<td style='border: 1px solid ")
                                .append(rectStroke).append("; text-align: center; vertical-align: middle;'>");

                        int valor = matrizCard[r][c];
                        if (valor == 0) {
                            pageHtmlBuilder
                                .append("<div style='display: flex; justify-content: center; align-items: center; width: 100%; height: 100%;'>")
                                .append("<svg width='").append(starFontSize).append("' height='").append(starFontSize)
                                .append("' viewBox='0 0 24 24' fill='#f59e0b' xmlns='http://www.w3.org/2000/svg'>")
                                .append("<path d='M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z'/>")
                                .append("</svg>")
                                .append("</div>");
                        } else {
                            pageHtmlBuilder.append(valor);
                        }
                        pageHtmlBuilder.append("</td>");
                    }
                    pageHtmlBuilder.append("</tr>");
                }
                pageHtmlBuilder.append("</table>");

                // Tratamento das strings dos Prêmios evitando NullPointer
                String awardName = (matriz.has("awardName") && !matriz.get("awardName").isNull()) ? matriz.get("awardName").asText() : "";
                String awardDonor = (matriz.has("awardDonor") && !matriz.get("awardDonor").isNull()) ? matriz.get("awardDonor").asText() : "";

                if (!awardName.isEmpty()) {
                    pageHtmlBuilder.append("<div style='text-align: center; color: ")
                            .append(textColor).append("; font-family: ").append(fontFamily)
                            .append("; font-weight: bold; ")
                            .append("font-size: ").append(awardFontSize).append("px; ")
                            .append("margin-top: ").append(15 * scaleY).append("px;'>") // Espaçamento escalado
                            .append(cardIndex + 1)
                            .append("° Prêmio: ")
                            .append(awardName)
                            .append("</div>");
                }
                if (!awardDonor.isEmpty()) {
                    pageHtmlBuilder
                            .append("<div style='text-align: center; color: ").append(textColor)
                            .append("; font-family: ").append(fontFamily)
                            .append("; font-weight: normal; ")
                            .append("font-size: ").append(awardFontSize).append("px; ")
                            .append("margin-top: ").append(5 * scaleY).append("px;'>") // Espaçamento escalado
                            .append("Doador(es): ")
                            .append(awardDonor)
                            .append("</div>");
                }

                pageHtmlBuilder.append("</div>"); // Fecha a .card

                cardIndex++;
            }
        } catch (Exception e) {
            throw new IOException("Erro ao processar as configurações de estilização e layout do JSON.", e);
        }

        pageHtmlBuilder.append("</div>");
        pageHtmlBuilder.append("</div>");

        String htmlBase;
        try {
            htmlBase = Files.readString(Path.of(TEMPLATE_PATH));
        } catch (IOException e) {
            throw new IOException("Erro ao ler o template HTML: " + TEMPLATE_PATH, e);
        }

        String cardPDF = htmlBase.replace("{{PAGES_DATA}}", pageHtmlBuilder.toString());

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(cardPDF, target);

        return target.toByteArray();
    }

    public List<int[][]> createMatrizesPresentInCard(Card card){
        List<int[][]> matrizesPresentInCard = new ArrayList<>();

        int[][] matrizCard = new int[5][5];
        List<NumberCard> numberCards = numberCardService.findNumberCardByIdCard(card.getId());
        for (NumberCard numberCard : numberCards){
            if (numberCard.getNumber() != null){
                matrizCard[numberCard.getRow()][numberCard.getColumn()] = numberCard.getNumber().getValue();
            }
            else{
                matrizCard[numberCard.getRow()][numberCard.getColumn()] = 0;
            }
        }
        matrizesPresentInCard.add(matrizCard);

        return matrizesPresentInCard;
    }

    public Card generateCard(CardDTO cardRequest) {
        Project project = projectRepository.findById(cardRequest.project()).orElseThrow(
                () -> new RuntimeException("Projeto não encontrado.")
        );

        Card newCard = new Card();

        newCard.setProject(project);
        newCard.setCodeCard(UUID.randomUUID().toString());

        newCard = cardRepository.save(newCard);

        numberCardService.createNumberCards(newCard);

        return newCard;
    }

    private byte[] mergePdfs(List<byte[]> pdfsToMerge) throws IOException {
        if (pdfsToMerge == null || pdfsToMerge.isEmpty()) {
            return new byte[0];
        }

        try (ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(mergedOutputStream);
            try (PdfDocument pdfDocument = new PdfDocument(writer)) {
                PdfMerger merger = new PdfMerger(pdfDocument);

                for (byte[] pdfBytes : pdfsToMerge) {
                    try (PdfDocument sourcePdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)))) {
                        merger.merge(sourcePdf, 1, sourcePdf.getNumberOfPages());
                    }
                }

                pdfDocument.close();
            }

            return mergedOutputStream.toByteArray();
        }
    }

    public String gerarTableHtml(int[][] matriz, String nameAward) {
        StringBuilder sb = new StringBuilder();

        sb.append("<table class='bingo-card'>");

        // Cabeçalho BINGO
        sb.append("<tr class='bingo-header'>");
        String[] letras = {"B", "I", "N", "G", "O"};
        for (String letra : letras) {
            sb.append("<td>").append(letra).append("</td>");
        }
        sb.append("</tr>");

        // Corpo da tabela
        for (int[] linha : matriz) {
            sb.append("<tr>");
            for (int valor : linha) {
                if (valor == 0) {
                    sb.append("<td class='bingo-center'>Free</td>");
                } else {
                    sb.append("<td>").append(valor).append("</td>");
                }
            }
            sb.append("</tr>");
        }

        sb.append("</table>");

        // Nome do prêmio (se houver)
        if (nameAward != null && !nameAward.isEmpty()) {
            sb.append("<p class='award-name'>").append(nameAward).append("</p>");
        }

        return sb.toString();
    }

}
