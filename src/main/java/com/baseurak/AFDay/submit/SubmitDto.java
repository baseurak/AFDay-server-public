package com.baseurak.AFDay.submit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor
public class SubmitDto {
    private Long Id;
    private Long quizGroupId;
    private String title;
    private Long userId;
    private String answers;
    private Long score;
    private String comment;
    private Timestamp submitDate;
}
