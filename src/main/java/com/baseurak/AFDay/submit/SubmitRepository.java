package com.baseurak.AFDay.submit;

import com.baseurak.AFDay.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmitRepository extends JpaRepository<Submit, Long> {
    List<Submit> findAllQuizListByQuizGroupId(Long quizGroupId);
    List<Submit> findAllQuizListByUserId(Long userId);
}
