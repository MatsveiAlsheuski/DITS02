package com.example.dits.aspect;

import com.example.dits.entity.Test;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class LogAspectTestService {
    @Before("execution(void com.example.dits.service.TestService.save(..))")
    public void beforeTestServiceMethod(JoinPoint joinPoint) {
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
        Object[] lArgs = joinPoint.getArgs();
        Test test = (Test) lArgs[0];
        return String.format("%-10s Test  : test  name = %-30s topic name = %-30s",
                joinPoint.getSignature().getName(), test.getName(), test.getTopic().getName());
    }

    private String createDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS --- ");
        return dateFormat.format(date);
    }
}