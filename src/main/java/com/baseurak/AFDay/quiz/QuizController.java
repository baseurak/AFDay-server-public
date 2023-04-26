package com.baseurak.AFDay.quiz;

import com.baseurak.AFDay.quiz.dto.QuizDto;
import com.baseurak.AFDay.quiz.dto.QuizGroupDto;
import com.baseurak.AFDay.quiz.entity.QuizGroup;
import com.baseurak.AFDay.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class QuizController {

    @Autowired
    QuizService quizService;
    @Autowired
    UserService userService;

    /**
     * GET - /quizGroup 요청 시 사용자가 만든 문제집을 모두 가져옵니다.
     */
    @GetMapping("/quiz-group")
    public List<QuizGroupDto> readQuizGroupDto() {
        return quizService.readQuizGroupDtoList(userService.findUserBySessionId());
    }

    /**
     * POST - /quizGroup 요청 시 새로운 문제집(퀴즈그룹)을 생성합니다.
     */
    @PostMapping("/quiz-group")
    public QuizGroupDto writeQuizDtoList(){
        return quizService.createQuizGroup(userService.findUserBySessionId());
    }

    /**
     * PUT - /quizGroup 요청 시 문제집 이름을 변경합니다.
     * @Param String title: 문제집 이름
     */
    @PutMapping("/quiz-group")
    public QuizGroupDto modifyQuizGroupDto(Long quizGroupId, String title){
        return quizService.modifyQuizGroup(quizGroupId, title, userService.findUserBySessionId());
    }

    /**
     * DELETE - /quizGroup 요청 시 quizGroupId에 해당하는 문제집(퀴즈그룹)을 삭제합니다.
     **/
    @DeleteMapping("/quiz-group")
    public void deleteQuizGroup(Long quizGroupId){
        quizService.deleteQuizGroup(quizGroupId, userService.findUserBySessionId());
    }

    /**
     * GET - /quiz/{quizGroupId} 요청 시 quizGroupId에 해당하는 퀴즈를 모두 가져옵니다.
     */
    @GetMapping("/quiz/{quizGroupId}")
    public QuizGroupDto readQuizDtoList(@PathVariable("quizGroupId") Long quizGroupId){
        return quizService.readQuizGroupDtoByQuizGroupId(quizGroupId);
    }

    /**
     * GET - /detail/{quizId} 요청 시 quizId에 해당하는 퀴즈를 가져옵니다.
     */
    @GetMapping("/detail/{quizId}")
    public QuizDto readQuizDto(@PathVariable("quizId") Long quizId) {
        return quizService.readQuizDto(quizId);
    }

    /**
     * POST - /quiz 요청 시 새로운 퀴즈를 저장합니다.
     * @Param Long quizGroupId: 퀴즈 그룹
     * @Param String quizType: 퀴즈 종류
     * @Param String question: 문제
     * @Param String answers: 정답
     * @Param List<QuizItem> items: 보기
     */
    @PostMapping("/quiz")
    public void write(QuizDto quizDto){
        quizService.write(quizDto, userService.findUserBySessionId());
    }

    /**
     * PUT - /quiz 요청 시 quizId에 해당하는 퀴즈를 수정합니다.
     * @Param Long quizId
     * @Param Long quizGroupId: 퀴즈 그룹
     * @Param String quizType: 퀴즈 종류
     * @Param String question: 문제
     * @Param String answers: 정답
     * @Param List<QuizItem> items: 보기
     */
    @PutMapping("/quiz")
    public void modify(QuizDto quizDto){
        quizService.modify(quizDto, userService.findUserBySessionId());
    }

    /**
     * DELETE - /quiz 요청 시 quizId에 해당하는 퀴즈를 삭제합니다.
     */
    @DeleteMapping("/quiz")
    public void delete(Long quizId){
        quizService.delete(quizId, userService.findUserBySessionId());
    }
}
