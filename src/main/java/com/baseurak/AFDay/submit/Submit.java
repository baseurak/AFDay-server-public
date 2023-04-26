package com.baseurak.AFDay.submit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Submit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quizGroupId;
    private String title;
    private Long userId;
    private String answers;
    private Long score;
    private String comment;
    private Timestamp submitDate;

    public Submit(SubmitDto submitDto) {
        this.userId = submitDto.getUserId();
        this.quizGroupId = submitDto.getQuizGroupId();
        this.title = submitDto.getTitle();
        this.answers = submitDto.getAnswers();
        this.score = submitDto.getScore();
        this.comment = submitDto.getComment();
        this.submitDate = submitDto.getSubmitDate();
    }
}
