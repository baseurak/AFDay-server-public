package com.baseurak.AFDay.quiz.repository;

import com.baseurak.AFDay.quiz.entity.QuizGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizGroupRepository  extends JpaRepository<QuizGroup, Long> {
    List<QuizGroup> findByUserId(Long userId);
    QuizGroup findOneById(Long id);
}