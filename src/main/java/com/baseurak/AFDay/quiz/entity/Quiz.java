package com.baseurak.AFDay.quiz.entity;

import com.baseurak.AFDay.quiz.QuizStatus;
import com.baseurak.AFDay.quiz.QuizType;
import com.baseurak.AFDay.quiz.dto.QuizDto;
import com.baseurak.AFDay.quiz.repository.QuizGroupRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private QuizType quizType;
    private Long sequenceNumber; //->sequence_number
    private String question;
    private String answers;

    @Enumerated(EnumType.STRING)
    private QuizStatus status;
    @OneToMany(mappedBy = "quiz")
    private List<QuizItem> items;

    private Long quizGroupId;

    public Quiz(QuizDto quizDto){
        this.quizType = quizDto.getQuizType();
        this.question = quizDto.getQuestion();
        this.sequenceNumber = quizDto.getSequenceNumber();
        this.answers = quizDto.getAnswers();
        this.quizGroupId = quizDto.getQuizGroupId();
        this.status = QuizStatus.CREATED;
    }

    public Quiz(){
        this.status = QuizStatus.CREATED;
    }

    public void setUserId(Long userId) {}

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setStatus(QuizStatus status){ this.status = status; }

    public void addItem(QuizItem item) {
        items.add(item);
    }

    public void setAnswers(String answers) { this.answers = answers;}
    public void setQuizGroupId(Long quizGroupId) { this.quizGroupId = quizGroupId; }
}
