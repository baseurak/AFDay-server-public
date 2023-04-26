package com.baseurak.AFDay.submit;

import com.baseurak.AFDay.exception.NotFoundException;
import com.baseurak.AFDay.quiz.dto.QuizGroupDto;
import com.baseurak.AFDay.quiz.entity.QuizGroup;
import com.baseurak.AFDay.quiz.repository.QuizGroupRepository;
import com.baseurak.AFDay.user.User;
import com.baseurak.AFDay.user.UserRepository;
import com.baseurak.AFDay.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final QuizGroupRepository quizGroupRepository;
    private final UserRepository userRepository;

    /**
     * 불러오기
     */
    public List<ResultDto> readSubmitListByQuizGroup(Long quizGroupId) {
        List<Submit> allSubmitList = submitRepository.findAllQuizListByQuizGroupId(quizGroupId);
        List<ResultDto> resultDtos = new ArrayList<>();
        for (Submit submit : allSubmitList) {
            ResultDto resultDto = new ResultDto(submit);
            Optional<User> findUser = userRepository.findById(submit.getUserId());
            if (findUser.isEmpty()) resultDto.setUsername("탈퇴회원");
            else resultDto.setUsername(findUser.get().getName());
            resultDtos.add(resultDto);
        }
        return resultDtos;
    }

    /**
     * 저장하기
     */
    public SubmitDto write(SubmitDto submitDto) {
        submitDto.setSubmitDate(Timestamp.valueOf(LocalDateTime.now()));
        Submit submit = new Submit(submitDto);
        if (submit.getTitle() == null || submit.getTitle().isEmpty()){
            QuizGroup quizGroup = quizGroupRepository.findOneById(submit.getQuizGroupId());
            submit.setTitle(quizGroup.getTitle());
        }
        submitRepository.save(submit);
        submitDto.setId(submit.getId());
        return submitDto;
    }

    /**
     * 사용자가 푼 퀴즈 모두 가져오기
     */
    public List<Submit> readSubmitListByUser(User user) {
        return submitRepository.findAllQuizListByUserId(user.getId());
    }

    /**
     * 퀴즈 결과 가져오기
     */
    public ResultDto readSubmit(Long submitId) {
        Optional<Submit> submit = submitRepository.findById(submitId);
        if (Objects.isNull(submit)) throw new NotFoundException("퀴즈 제출 결과가 존재하지 않습니다.");
        ResultDto resultDto = new ResultDto(submit.get());
        Optional<User> user = userRepository.findById(resultDto.getUserId());
        if (Objects.isNull(user)) resultDto.setUsername("탈퇴회원");
        else resultDto.setUsername(user.get().getName());
        return resultDto;
    }
}