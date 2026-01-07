package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.exception.custom.InvalidAwardAmountException;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.project.StyleCard;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.card.CardRepository;
import com.bingofacil.bingofacil.repositories.card.NumberCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.award.AwardService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    public byte[] createCardPDF (CardDTO cardRequest, StyleCard typeCards, List<Award> awardsProject,
                                 String nameProject, String dataProject, String timeProject, String valueProject
                                 ) throws IOException {
        Card card = generateCard(cardRequest);

        String codeCard = card.getCodeCard();

        List<int[][]> MatrizesPresentInPDF = createMatrizesPresentInCard(card);
        StringBuilder allTablesHtml = new StringBuilder();

        if (StyleCard.SINGLE_CARD_FOR_ALL_AWARDS.equals(typeCards)){
            for (int[][] matrizCard : MatrizesPresentInPDF){
                String tableHtml = gerarTableHtml(matrizCard, null);
                allTablesHtml.append(tableHtml).append(PAGE_BREAK);
            }
        } else{
            for (int[][] matrizCard : MatrizesPresentInPDF) {
                String tableHtml = null;
                for (Award award : awardsProject) {
                    tableHtml = gerarTableHtml(matrizCard, award.getName());
                    allTablesHtml.append(tableHtml).append(PAGE_BREAK);
                }
            }
        }

        String htmlBase;
        try {
            htmlBase = Files.readString(Path.of(TEMPLATE_PATH));
        } catch (IOException e) {
            throw new IOException("Erro ao ler o template HTML: " + TEMPLATE_PATH, e);
        }

        String cardPDF = htmlBase
                .replace(PLACEHOLDER_NUMBER_CARD, allTablesHtml.toString())
                .replace(PLACEHOLDER_CODE_CARD, codeCard)
                .replace(PLACEHOLDER_NAME_PROJECT ,nameProject)
                .replace(PLACEHOLDER_DATA_PROJECT, dataProject)
                .replace(PLACEHOLDER_TIME_PROJECT, timeProject)
                .replace(PLACEHOLDER_VALUE_PROJECT, valueProject)
                ;

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
