package com.baseurak.AFDay.quiz.dto;

import com.baseurak.AFDay.quiz.entity.QuizGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class QuizGroupDto {
    private Long id;
    private Long userId;
    private String title;
    private Timestamp updateDate;
    private List<QuizDto> quizzes;

    public QuizGroupDto(QuizGroup quizGroup) {
        this.id = quizGroup.getId();
        this.userId = quizGroup.getUserId();
        this.title = quizGroup.getTitle();
        this.updateDate = quizGroup.getUpdateDate();
        this.quizzes = new ArrayList<>();
    }
}