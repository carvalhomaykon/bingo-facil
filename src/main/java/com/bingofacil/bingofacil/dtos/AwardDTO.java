package com.bingofacil.bingofacil.dtos;

import com.bingofacil.bingofacil.model.award.StyleAward;

public record AwardDTO (String name, Integer amount, Long project, StyleAward styleAward){
}
