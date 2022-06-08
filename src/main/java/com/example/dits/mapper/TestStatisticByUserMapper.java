package com.example.dits.mapper;

import com.example.dits.dto.TestStatisticByUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestStatisticByUserMapper {

    public TestStatisticByUser convertToTestStatisticByUser(String testName,int count, int avgProc){
        return TestStatisticByUser.builder()
                .testName(testName)
                .count(count)
                .avgProc(avgProc)
                .build();
    }
}
