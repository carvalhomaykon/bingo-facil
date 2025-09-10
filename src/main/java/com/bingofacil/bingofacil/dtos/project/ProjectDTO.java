package com.bingofacil.bingofacil.dtos.project;

import com.bingofacil.bingofacil.model.project.StatusProject;
import com.bingofacil.bingofacil.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectDTO (String name, Integer amountAwards, LocalDateTime dateAndTime, String description, BigDecimal value, StatusProject status, User organizer){
}
