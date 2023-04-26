package com.baseurak.AFDay.quiz;

import com.baseurak.AFDay.exception.BadRequestException;
import com.baseurak.AFDay.exception.ForbiddenException;
import com.baseurak.AFDay.exception.NotFoundException;
import com.baseurak.AFDay.quiz.dto.QuizDto;
import com.baseurak.AFDay.quiz.dto.QuizGroupDto;
import com.baseurak.AFDay.quiz.entity.Quiz;
import com.baseurak.AFDay.quiz.entity.QuizGroup;
import com.baseurak.AFDay.quiz.entity.QuizItem;
import com.baseurak.AFDay.quiz.repository.QuizGroupRepository;
import com.baseurak.AFDay.quiz.repository.QuizItemRepository;
import com.baseurak.AFDay.quiz.repository.QuizRepository;
import com.baseurak.AFDay.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.baseurak.AFDay.quiz.QuizStatus.CREATED;
import static com.baseurak.AFDay.quiz.QuizType.MULTIPLE_CHOICE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizItemRepository quizItemRepository;
    private final QuizGroupRepository quizGroupRepository;

    /**
     * user이 작성한 문제집을 모두 반환합니다.
     */
    public List<QuizGroupDto> readQuizGroupDtoList(User user) {
        List<QuizGroup> findQuizGroups = quizGroupRepository.findByUserId(user.getId());
        List<QuizGroupDto> quizGroupDtos = new ArrayList<>();
        for (QuizGroup findQuizGroup : findQuizGroups) {
            quizGroupDtos.add(new QuizGroupDto(findQuizGroup));
        }
        return quizGroupDtos;
    }

    /**
     * quizGroupId에 해당하는 QuizGroupDto를 반환합니다.
     * @return
     */
    public QuizGroupDto readQuizGroupDtoByQuizGroupId(Long quizGroupId) {
        QuizGroupDto quizGroupDto = new QuizGroupDto(quizGroupRepository.findOneById(quizGroupId));
        quizGroupDto.setQuizzes(readQuizDtoListByQuizGroupId(quizGroupId));
        return quizGroupDto;
    }

    /**
     * quizGroupId에 해당하는 퀴즈Dto리스트를 반환합니다.
     */
    private List<QuizDto> readQuizDtoListByQuizGroupId(Long quizGroupId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sequenceNumber");
        List<Quiz> findQuizs = quizRepository.findAllQuizListByQuizGroupIdAndStatus(quizGroupId, CREATED, sort);
        List<QuizDto> quizDtos = new ArrayList<>();
        for (Quiz quiz : findQuizs) {
            quizDtos.add(new QuizDto(quiz));
        }
        return quizDtos;
    }

    /**
     * quizGroupId에 해당하는 퀴즈를 반환합니다 (유닛테스트용)
     */
    public List<Quiz> readQuizListByQuizGroupId(Long quizGroupId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sequenceNumber");
        return quizRepository.findAllQuizListByQuizGroupIdAndStatus(quizGroupId, CREATED, sort);
    }

    /**
     * 문제집(퀴즈 그룹)을 생성합니다.
     */
    public QuizGroupDto createQuizGroup(User user) {
        QuizGroup quizGroup = new QuizGroup();
        quizGroup.setTitle("이름 없는 문제집");
        quizGroup.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        quizGroup.setUserId(user.getId());
        quizGroupRepository.save(quizGroup);

        // 저장된 QuizGroup 객체를 다시 DB에서 조회하여 QuizGroupDto 객체 생성
        QuizGroup savedQuizGroup = quizGroupRepository.findOneById(quizGroup.getId());
        return new QuizGroupDto(savedQuizGroup);
    }

    /**
     * quizGroupId에 해당하는 문제집 이름을 수정합니다.
     */
    public QuizGroupDto modifyQuizGroup(Long quizGroupId, String title, User user) {
        if (!checkUser(quizGroupId, user)) throw new ForbiddenException("사용자 정보가 다릅니다.");
        QuizGroup findQuizGroup = quizGroupRepository.findOneById(quizGroupId);
        findQuizGroup.setTitle(title);
        quizGroupRepository.save(findQuizGroup);
        return new QuizGroupDto(findQuizGroup);
    }

    /**
     * quizGroupId에  해당하는 문제집을 삭제합니다.
     */
    public void deleteQuizGroup(Long quizGroupId, User user) {
        if (!checkUser(quizGroupId, user)) throw new ForbiddenException("사용자 정보가 다릅니다.");
        quizGroupRepository.delete(quizGroupRepository.findOneById(quizGroupId));
    }

    /**
     * quizId에 해당하는 퀴즈를 퀴즈DTO로 반환합니다.
     */
    public QuizDto readQuizDto(Long quizId) {
        Quiz findQuiz = quizRepository.findOneById(quizId);
        if (Objects.isNull(findQuiz)) throw new NotFoundException("퀴즈를 찾을 수 없습니다.");
        else return new QuizDto(findQuiz);
    }

    /**
     * quizId에 해당하는 퀴즈를 가져옵니다.
     */
    public Quiz readQuiz(Long quizId) {
        Quiz findQuiz = quizRepository.findOneById(quizId);
        if (Objects.isNull(findQuiz)) throw new NotFoundException("퀴즈를 찾을 수 없습니다.");
        else return findQuiz;
    }

    /**
     * quizDto와 사용자 정보를 받아 새로운 퀴즈를 생성합니다.
     */
    public Quiz write(QuizDto quizDto, User user) {
        QuizGroup findQuizGroup = quizGroupRepository.findOneById(quizDto.getQuizGroupId());

        //퀴즈 번호
        long sequenceNumber = quizRepository.countByQuizGroupId(quizDto.getQuizGroupId());
        quizDto.setSequenceNumber(sequenceNumber+1);

        return createQuiz(quizDto, user);
    }

    /**
     * quizDto와 사용자 정보를 받아 퀴즈를 수정합니다.
     */
    public void modify(QuizDto quizDto, User user) {
        if (!checkUser(quizDto.getQuizGroupId(), user)) throw new ForbiddenException("사용자 정보가 다릅니다.");

        //기존 퀴즈 찾기
        if (quizDto.getQuizId() == null) throw new BadRequestException("quizId가 누락되었습니다.");
        Quiz findQuiz = quizRepository.findOneById(quizDto.getQuizId());
        if (Objects.isNull(findQuiz)) throw new NotFoundException("퀴즈를 찾을 수 없습니다.");

        //기존 값 채우기
        quizDto.setSequenceNumber(findQuiz.getSequenceNumber());
        quizDto.setQuizType(findQuiz.getQuizType());
        if (quizDto.getQuizGroupId() == null)
            quizDto.setQuizGroupId(findQuiz.getQuizGroupId());

        //문제가 비어있다면 기존 퀴즈로 채우기
        if (Objects.isNull(quizDto.getQuestion())){
            quizDto.setQuestion(findQuiz.getQuestion());
        }

        //보기가 비어있다면 기존 퀴즈로 채우기
        if (Objects.isNull(quizDto.getItems())){
            List<String> quizItemList = new ArrayList<>();
            for (QuizItem item : findQuiz.getItems()) {
                quizItemList.add(item.getItem());
            }
            quizDto.setItems(quizItemList);
        }

        //정답이 비어있다면 기존 문제로 채우기
        if (Objects.isNull(quizDto.getAnswers())){
            quizDto.setAnswers(findQuiz.getAnswers());
        }

        //기존 퀴즈 삭제처리
        findQuiz.setStatus(QuizStatus.DELETED);

        //새로운 퀴즈 생성
        createQuiz(quizDto, user);
    }

    private Quiz createQuiz(QuizDto quizDto, User user){
        if (quizDto.getQuizGroupId() == null) {
            throw new BadRequestException("quizId가 누락되었습니다.");
        }
        QuizGroup quizGroup = quizGroupRepository.findOneById(quizDto.getQuizGroupId());
        quizGroup.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));

        //퀴즈 저장
        if (quizDto.getQuestion() == null || quizDto.getQuestion().isEmpty()) {
            throw new BadRequestException("question이 누락되었습니다.");
        }
        Quiz quiz = new Quiz(quizDto);
        quiz.setQuizGroupId(quizDto.getQuizGroupId());
        quizRepository.save(quiz);

        //보기 저장
        if (quizDto.getQuizType() == MULTIPLE_CHOICE) {
            if (Objects.isNull(quizDto.getItems())){
                throw new BadRequestException("items가 누락되었습니다.");
            } else {
                for (int i = 0; i < quizDto.getItems().size(); i++) {
                    QuizItem quizItem = new QuizItem(quiz, (long) i + 1, quizDto.getItems().get(i));
                    quizItemRepository.save(quizItem);
                }
            }
        }
        return quiz;
    }

    /**
     * quizId에 해당하는 퀴즈를 삭제합니다.
     */
    public void delete(Long quizId, User user) {
        Quiz findQuiz = quizRepository.findOneById(quizId);
        if (!checkUser(findQuiz.getQuizGroupId(), user)) throw new ForbiddenException("사용자 정보가 다릅니다.");
        findQuiz.setStatus(QuizStatus.DELETED);
    }

    /**
     * quizId에 해당하는 퀴즈의 작성자와 user가 같은지 확인합니다.
     */
    public Boolean checkUser(Long quizGroupId, User user){
        QuizGroup findQuizGroup = quizGroupRepository.findOneById(quizGroupId);
        return findQuizGroup.getUserId().equals(user.getId());
    }
}