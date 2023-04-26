package com.baseurak.AFDay.quiz.repository;

import com.baseurak.AFDay.quiz.QuizStatus;
import com.baseurak.AFDay.quiz.QuizType;
import com.baseurak.AFDay.quiz.entity.Quiz;
import com.baseurak.AFDay.quiz.entity.QuizItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
    List<Quiz> findAllQuizListByQuizGroupId(Long quizGroupId);
    List<Quiz> findAllQuizListByQuizGroupIdAndStatus(Long quizGroupId, QuizStatus quizStatus, Sort sort);
    Quiz findOneById(Long quizId);
    long countByQuizGroupId(Long quizGroupId);
}
