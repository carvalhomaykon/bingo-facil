package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.NumberCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.card.CardRepository;
import com.bingofacil.bingofacil.repositories.card.NumberBingoRepository;
import com.bingofacil.bingofacil.repositories.card.NumberCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.award.AwardService;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private static final String TYPE_CARD_ALL_AWARDS = "1";
    private static final String PAGE_BREAK = "<br style=\"page-break-before: always;\">";
    private static final String TEMPLATE_PATH = "src/main/resources/templates/cardsBingo.html";
    private static final String PLACEHOLDER_ALL_CARDS = "{{ALL_CARDS}}";

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

    // Pegar card pelo id
    public Card findCardById(Long id){
        return cardRepository.findById(id).orElse(null);
    }

    // Pegar todos os cards pelo id do usuário
    public List<Card> findCardsByIdUser(Long userId){
        return cardRepository.findAllByUserId(userId);
    }

    // Pegar todos os cards pelo id do project
    public List<Card> findCardsByIdProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project não encontrado.")
        );

        return cardRepository.findAllByProjectId(project.getId());
    }

    public List<Card> generateCards(int amount, CardDTO cardRequest) {
        Project project = projectRepository.findById(cardRequest.project()).orElseThrow(
                () -> new RuntimeException("Projeto não encontrado.")
        );

        User user = project.getOrganizer();

        List<Card> cards = new ArrayList<>();

        // 2. Loop para criar a quantidade desejada de cards
        for (int i = 0; i < amount; i++) {
            Card newCard = new Card();

            // Usar os objetos User e Project validados
            newCard.setUser(user);
            newCard.setProject(project);

            // 3. Salvar o Card (gera o ID)
            newCard = cardRepository.save(newCard);

            // 4. Criar os números do card
            numberCardService.createNumberCards(newCard);

            cards.add(newCard);
        }

        // 5. Retornar a lista de cards gerados
        return cards;
    }

    public byte[] generateCardsPDF(int amount, CardDTO requestCard, String typeCards) throws IOException {
        Project projectCard = projectRepository.findById(requestCard.project()).orElseThrow(
                () -> new EntityNotFoundException("Projeto com ID " + requestCard.project() + " não encontrado.")
        );

        List<Award> awardsProject = awardService.findByProjectId(projectCard.getId());

        if (projectCard.getAmountAwards() < awardsProject.size()) {
            throw new RuntimeException("Número informado de prêmios no projeto é menor que a quantidade de prêmios cadastrados.");
        }

        String htmlBase;
        try {
            htmlBase = Files.readString(Path.of(TEMPLATE_PATH));
        } catch (IOException e) {
            throw new IOException("Erro ao ler o template HTML: " + TEMPLATE_PATH, e);
        }

        List<int[][]> MatrizesPresentInPDF = createMatrizesPresentInPDF(amount, requestCard);
        StringBuilder allTablesHtml = new StringBuilder();

        if (TYPE_CARD_ALL_AWARDS.equals(typeCards)){
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

        String htmlFinal = htmlBase.replace(PLACEHOLDER_ALL_CARDS, allTablesHtml.toString());
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(htmlFinal, target);

        byte[] bytes = target.toByteArray();
        return bytes;
    }

    public List<int[][]> createMatrizesPresentInPDF(int amount, CardDTO requestCard){
        List<Card> cards = generateCards(amount, requestCard);
        List<int[][]> matrizesPresentInPDF = new ArrayList<>();

        for (Card card : cards){
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
            matrizesPresentInPDF.add(matrizCard);
        }

        return matrizesPresentInPDF;
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
            sb.append("<p class='award-name'>🏆 ").append(nameAward).append("</p>");
        }

        return sb.toString();
    }

}
