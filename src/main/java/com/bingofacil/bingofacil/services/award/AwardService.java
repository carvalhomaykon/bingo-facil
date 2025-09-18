package com.bingofacil.bingofacil.services.award;

import com.bingofacil.bingofacil.dtos.AwardDTO;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.repositories.award.AwardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Create
    public Award createAward(AwardDTO dto){
        Award award = new Award(dto);

        Project project = projectRepository.findById(dto.project())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        award.setProject(project);

        return awardRepository.save(award);
    }

    // Read
    public Award findAwardById(Long id){
        return awardRepository.findById(id).orElse(null);
    }

    public List<Award> findByProjectId(Long idProject){
        return awardRepository.findByProjectId(idProject);
    }

    // Delete
    public void removeAward(Long id){
        awardRepository.deleteById(id);
    }

    // Edite
    public Award editAward(Long id, AwardDTO dto){
        Award award = this.awardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prêmio não encontrado"));

        award.setName(dto.name());
        award.setAmount(dto.amount());
        award.setStyleAward(dto.styleAward());

        return this.awardRepository.save(award);

    }

}
