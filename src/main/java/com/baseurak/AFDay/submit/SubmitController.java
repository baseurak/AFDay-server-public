package com.baseurak.AFDay.submit;

import com.baseurak.AFDay.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubmitController {

    @Autowired SubmitService submitService;
    @Autowired UserService userService;
    /**
     * POST - /score/{score} 요청 시 사용자의 퀴즈 점수를 저장합니다.
     * @Param Long quizGroupId: 퀴즈 그룹
     * @Param String answers: 정답
     * @Param Long score: 점수
     * @Param String comment: 하고싶은말
     */
    @PostMapping("/score")
    public SubmitDto writeScore(SubmitDto submitDto){
        submitDto.setUserId(userService.findUserBySessionId().getId());
        return submitService.write(submitDto);
    }

    /**
     * GET - /score/{quizGroupId} 요청 시 quizGroupId에 해당하는 퀴즈를 푼 결과를 모두 가져옵니다.
     */
    @GetMapping("/score/{quizGroupId}")
    public List<ResultDto> readScoreListByQuizGroupId(@PathVariable("quizGroupId") Long quizGroupId){
        return submitService.readSubmitListByQuizGroup(quizGroupId);
    }

    /**
     * GET - /result/{submitId} 요청 시 submitId에 해당하는 퀴즈를 푼 결과를 가져옵니다.
     */
    @GetMapping("/result/{submitId}")
    public ResultDto readSubmitBySubmitId(@PathVariable("submitId") Long submitId){
        return submitService.readSubmit(submitId);
    }

    /**
     * GET - /record 요청 시 사용자가 푼 퀴즈 결과를 모두 가져옵니다.
     */
    @GetMapping("/record")
    public List<Submit> readScoreListByUser() {
        return submitService.readSubmitListByUser(userService.findUserBySessionId());
    }
}
