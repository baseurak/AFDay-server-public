package com.baseurak.AFDay.submit;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class ResultDto {
    private Long id;
    private String username;
    private Long quizGroupId;
    private String title;
    private Long userId;
    private String answers;
    private Long score;
    private String comment;
    private Timestamp submitDate;

    public ResultDto(Submit submit) {
        this.id = submit.getId();
        this.userId = submit.getUserId();
        this.quizGroupId = submit.getQuizGroupId();
        this.title = submit.getTitle();
        this.answers = submit.getAnswers();
        this.score = submit.getScore();
        this.comment = submit.getComment();
        this.submitDate = submit.getSubmitDate();
    }
}
