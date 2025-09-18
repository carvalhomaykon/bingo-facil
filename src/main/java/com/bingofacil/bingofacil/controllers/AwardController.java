package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.AwardDTO;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.services.award.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/awards")
public class AwardController {

    @Autowired
    private AwardService awardService;

    @PostMapping
    public ResponseEntity<Award> createAward(@RequestBody AwardDTO award){
        Award newAward = awardService.createAward(award);
        return new ResponseEntity<>(newAward, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Award> findAwardById(@PathVariable Long id){
        Award award = awardService.findAwardById(id);
        return new ResponseEntity<>(award, HttpStatus.OK);
    }

    @GetMapping("project/{idProject}")
    public ResponseEntity<List<Award>> findByProjectId(@PathVariable Long idProject){
        List<Award> awards = awardService.findByProjectId(idProject);
        return new ResponseEntity<>(awards, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void removeAward(@PathVariable Long id){
        awardService.removeAward(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Award> editProject(@PathVariable Long id, @RequestBody AwardDTO award){
        Award editAward = awardService.editAward(id, award);

        return new ResponseEntity<>(editAward, HttpStatus.OK);
    }

}
