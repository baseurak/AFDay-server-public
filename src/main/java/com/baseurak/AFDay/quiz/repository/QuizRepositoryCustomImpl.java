package com.baseurak.AFDay.quiz.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;

public class QuizRepositoryCustomImpl implements QuizRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public QuizRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
}
