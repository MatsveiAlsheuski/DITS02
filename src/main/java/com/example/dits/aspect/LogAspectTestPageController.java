package com.example.dits.aspect;

import com.example.dits.entity.User;
import org.apache.catalina.session.StandardSessionFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Aspect
@Component
@Profile("!test")
public class LogAspectTestPageController {

    @After("execution(String com.example.dits.controllers.TestPageController.goTest(..))")
    public void afterGoTestMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassageForGoTest(joinPoint);
        writerToFile(massage);
    }
    @After("execution(String com.example.dits.controllers.TestPageController.nextTestPage(..))")
    public void afterNextTestPageControllerMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassageForGoTest(joinPoint);
        writerToFile(massage);
    }
    @After("execution(String com.example.dits.controllers.TestPageController.testStatistic(..))")
    public void afterTestStatisticControllerMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassageForStatistic(joinPoint);
        writerToFile(massage);
    }

    private void writerToFile(String massage) {
        String fileOrders = "src/main/resources/temp.txt";
        try {
            Files.write(Paths.get(fileOrders), massage.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(fileOrders), "\n".getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createMassageForGoTest(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        StandardSessionFacade session = (StandardSessionFacade) lArgs[lArgs.length-1];
        User user = (User) session.getAttribute("user");
        String topicName = (String) session.getAttribute("topicName");
        String testName = (String) session.getAttribute("testName");
        String questionNumber = session.getAttribute("questionNumber").toString()+"/"
                + ((List)session.getAttribute("questions")).size();
        return String.format("%-12sUser :  userLogin = %-10s Test : topic name = %-15s test name = %-15s question = %-5s",
                joinPoint.getSignature().getName(), user.getLogin(), topicName, testName,questionNumber);
    }

    private String createMassageForStatistic(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        StandardSessionFacade session = (StandardSessionFacade) lArgs[lArgs.length-1];
        User user = (User) session.getAttribute("user");
        String topicName = (String) session.getAttribute("topicName");
        String testName = (String) session.getAttribute("testName");
        return String.format("Test ended  User :  userLogin = %-10s Test : topic name = %-15s test name = %-15s",
                user.getLogin(), topicName, testName);
    }

    private String createDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS --- ");
        return dateFormat.format(date);
    }
}