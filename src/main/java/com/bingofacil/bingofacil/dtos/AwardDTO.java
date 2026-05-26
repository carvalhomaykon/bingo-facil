package com.bingofacil.bingofacil.dtos;

import com.bingofacil.bingofacil.model.award.StyleAward;

public record AwardDTO (String name, String donor, Long project, StyleAward styleAward){
}
