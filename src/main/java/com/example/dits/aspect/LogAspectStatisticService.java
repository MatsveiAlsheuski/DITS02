package com.example.dits.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
@Profile("!test")
public class LogAspectStatisticService {
    @Before("execution(* com.example.dits.service.StatisticService.getStatisticsByUser(..))")
    public void beforeStatisticServiceMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassage(joinPoint);
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

    private String createMassage(JoinPoint joinPoint) {
        return String.format("get    Statistic : Do  method = %-15s",
                joinPoint.getSignature().getName());
    }

    private String createDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS --- ");
        return dateFormat.format(date);
    }
}