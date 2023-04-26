package com.baseurak.AFDay.quiz.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class QuizGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private Timestamp updateDate;

    public QuizGroup(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }
}
