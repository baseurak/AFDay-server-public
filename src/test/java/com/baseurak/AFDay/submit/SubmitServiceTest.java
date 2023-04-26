package com.baseurak.AFDay.submit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SubmitServiceTest {

    @Autowired SubmitService submitService;

    @BeforeEach
    public void beforeEach(){
        SubmitDto submitDto = new SubmitDto();
        SubmitDto write = submitService.write(submitDto);
        log.info("write = {}", write);
    }

    @Test
    public void read(){
        ResultDto findSubmit = submitService.readSubmit(50L);
        log.info("find = {}", findSubmit);
    }

}