package com.baseurak.AFDay.quiz.repository;

import com.baseurak.AFDay.quiz.entity.Quiz;
import com.baseurak.AFDay.quiz.entity.QuizItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizItemRepository extends JpaRepository<QuizItem, Long> {
}