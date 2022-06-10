package com.example.dits.mapper;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TestStatisticByUserMapper {

    public List<TestStatisticByUser> convertListToTestStatisticByUser(List<Statistic> testList){
        return testList.stream().map(statistic -> convertToTestStatisticByUser(
                getTest(statistic).getName(),
                countAllAttempt(testList, getTest(statistic)),
                percentCount(testList, getTest(statistic))
        )).distinct().collect(Collectors.toList());
    }

    public TestStatisticByUser convertToTestStatisticByUser(String testName,int count, int avgProc){
        return TestStatisticByUser.builder()
                .testName(testName)
                .count(count)
                .avgProc(avgProc)
                .build();
    }

    private Test getTest(Statistic statistic){
        return statistic.getQuestion().getTest();
    }

    private int countAllAttempt(List<Statistic> testList, Test test){
        return (int) testList.stream().filter(statistic -> getTest(statistic).equals(test)).count();
    }

    private int percentCount(List<Statistic> testList, Test test){
        return (100 * countCorrectAttempt(testList, test) / countAllAttempt(testList, test));
    }
    private int countCorrectAttempt(List<Statistic> testList, Test test){
        return (int) testList.stream().filter(statistic -> getTest(statistic).equals(test))
                .filter(statistic1 -> statistic1.isCorrect()==true).count();
    }
}
