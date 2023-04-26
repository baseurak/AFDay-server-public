package com.baseurak.AFDay.quiz.dto;

import com.baseurak.AFDay.quiz.QuizType;
import com.baseurak.AFDay.quiz.entity.Quiz;
import com.baseurak.AFDay.quiz.entity.QuizItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class QuizDto {
    private Long quizId;
    private Long quizGroupId;
    private QuizType quizType;
    private String question;
    private Long sequenceNumber;
    private List<String> items;
    private String answers;

    public QuizDto(Long quizGroupId, QuizType quizType, String question) {
        this.quizGroupId = quizGroupId;
        this.quizType = quizType;
        this.question = question;
    }

    public QuizDto(Quiz quiz){
        this.quizId = quiz.getId();
        this.quizType = quiz.getQuizType();
        this.question = quiz.getQuestion();
        this.answers = quiz.getAnswers();
        this.quizGroupId = quiz.getQuizGroupId();
        this.items = new ArrayList<>();
        for (QuizItem item : quiz.getItems()) {
            this.items.add(item.getItem());
        }
    }
}