package com.baseurak.AFDay.quiz;

import com.baseurak.AFDay.exception.NotFoundException;
import com.baseurak.AFDay.quiz.dto.QuizDto;
import com.baseurak.AFDay.quiz.dto.QuizGroupDto;
import com.baseurak.AFDay.quiz.entity.Quiz;
import com.baseurak.AFDay.quiz.entity.QuizGroup;
import com.baseurak.AFDay.quiz.entity.QuizItem;
import com.baseurak.AFDay.quiz.repository.QuizGroupRepository;
import com.baseurak.AFDay.quiz.repository.QuizRepository;
import com.baseurak.AFDay.user.Role;
import com.baseurak.AFDay.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class QuizServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired QuizService quizService;
    @Autowired QuizGroupRepository quizGroupRepository;
    @Autowired QuizRepository quizRepository;
    User user;
    Long quizGroupId;

    @BeforeEach
    public void beforeEach(){
        user = new User();
        user.setId(0L);
        user.setRole(Role.USER);

        QuizGroupDto quizGroup = quizService.createQuizGroup(user);
        em.flush(); //@PreUpdate
        em.clear();

        quizGroupId = quizGroup.getId();

        String answers = "{ANSWER1, ANSWER2}";
        List<String> items = new ArrayList<>();
        items.add("ITEM1");
        items.add("ITEM2");
        items.add("ITEM3");

        QuizDto quizDto1 = new QuizDto(user.getId(), QuizType.MULTIPLE_CHOICE, "TEST1");
        quizDto1.setAnswers(answers);
        quizDto1.setItems(items);
        quizDto1.setQuizGroupId(quizGroup.getId());
        quizService.write(quizDto1, user);

        QuizDto quizDto2 = new QuizDto(user.getId(), QuizType.MULTIPLE_CHOICE, "TEST2");
        quizDto2.setAnswers(answers);
        quizDto2.setItems(items);
        quizDto2.setQuizGroupId(quizGroup.getId());
        quizService.write(quizDto2, user);

        QuizDto quizDto3 = new QuizDto(user.getId(), QuizType.TRUE_OR_FALSE, "TEST3");
        quizDto3.setAnswers("O");
        quizDto3.setQuizGroupId(quizGroup.getId());
        quizService.write(quizDto3, user);

        em.flush(); //@PreUpdate
        em.clear();

        QuizGroup oneById = quizGroupRepository.findOneById(quizGroupId);
        log.info("quizGroup = {}", quizGroupRepository.findOneById(quizGroupId));
    }

    @Test
    void readQuizGroupList() {
        List<QuizGroupDto> findQuizGroups = quizService.readQuizGroupDtoList(user);
        assertThat(findQuizGroups.get(0).getId()).isEqualTo(quizGroupId);
    }

    @Test
    void modifyQuizGroup() {
        quizService.modifyQuizGroup(quizGroupId, "MODIFY TEST", user);
        List<QuizGroupDto> findQuizGroups = quizService.readQuizGroupDtoList(user);
        assertThat(findQuizGroups.get(0).getTitle()).isEqualTo("MODIFY TEST");
    }

    @Test
    void readQuizListByUserId() {
        List<Quiz> findQuizList = quizService.readQuizListByQuizGroupId(user.getId());
        for (Quiz quiz : findQuizList) {
            log.info("question = {}", quiz.getQuestion());
            log.info("findQuizItems = {}", quiz.getItems());
            log.info("findQuizAnswers = {}", quiz.getAnswers());
        }
    }

    @Test
    void modify() {
        QuizDto quizDto = new QuizDto();
        List<Quiz> findQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        quizDto.setQuizId(findQuizList.get(0).getId());
        quizDto.setQuizGroupId(quizGroupId);

        //문제 수정
        quizDto.setQuestion("MODIFY TEST");
        quizService.modify(quizDto, user);
        List<Quiz> changeQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        assertThat(changeQuizList.get(0).getQuestion()).isEqualTo("MODIFY TEST");
        quizDto.setQuestion(null);

        findQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        quizDto.setQuizId(findQuizList.get(0).getId());
        quizDto.setQuizGroupId(quizGroupId);

        //보기 수정
        List<String> items = new ArrayList<>();
        items.add("MODIFY ITEM1");
        items.add("MODIFY ITEM2");
        items.add("MODIFY ITEM3");
        quizDto.setItems(items);
        quizService.modify(quizDto, user);
        em.flush(); //@PreUpdate
        em.clear();

        changeQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        log.info("QuizItemList = {}", changeQuizList.get(0).getItems());
        List<QuizItem> changeQuizItems = changeQuizList.get(0).getItems();
        assertThat(changeQuizItems.get(0).getItem()).isEqualTo(items.get(0));
        assertThat(changeQuizItems.get(1).getItem()).isEqualTo(items.get(1));
        assertThat(changeQuizItems.get(2).getItem()).isEqualTo(items.get(2));
        quizDto.setItems(null);

        findQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        quizDto.setQuizId(findQuizList.get(0).getId());
        quizDto.setQuizGroupId(quizGroupId);

        //정답 수정
        quizDto.setAnswers("{MODIFY ANSWER1, MODIFY ANSWER2}");
        quizService.modify(quizDto, user);
        em.flush(); //@PreUpdate
        em.clear();

        changeQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        log.info("AnswerList = {}", changeQuizList.get(0).getAnswers());
        assertThat(changeQuizList.get(0).getAnswers()).isEqualTo("{MODIFY ANSWER1, MODIFY ANSWER2}");
    }

    @Test
    void delete() {
        List<QuizGroup> all = quizGroupRepository.findAll();
        List<Quiz> all1 = quizRepository.findAll();

        List<Quiz> findQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        Quiz quiz = findQuizList.get(0);
        quizService.delete(quiz.getId(), user);
//        assertThatThrownBy(() -> quizService.readQuiz(quiz.getId()))
//                .isInstanceOf(NotFoundException.class);
        List<Quiz> changeQuizList = quizService.readQuizListByQuizGroupId(quizGroupId);
        assertThat(changeQuizList.get(0)).isNotEqualTo(quiz);
    }
}