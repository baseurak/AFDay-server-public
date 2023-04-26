package com.baseurak.AFDay.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class QuizItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long sequenceNumber;
    private String item;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public QuizItem(Quiz quiz, Long sequenceNumber, String item){
        this.quiz = quiz;
        this.sequenceNumber = sequenceNumber;
        this.item = item;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "QuizItem{" +
                "id=" + id +
                ", sequenceNumber=" + sequenceNumber +
                ", item='" + item + '\'' +
                '}';
    }
}