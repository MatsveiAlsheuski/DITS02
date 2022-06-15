package com.example.dits.aspect;

import com.example.dits.entity.User;
import org.apache.catalina.session.StandardSessionFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
public class LogAspectAuthentication {

    @After("execution(* com.example.dits.controllers.SecurityController.loginHandle(..))")
    public void afterSecurityControllerMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassageLogin(joinPoint);
        writerToFile(massage);
    }

    @Before("execution(* com.example.dits.controllers.SecurityController.logoutPage(..))")
    public void beforeLogoutPageMethod(JoinPoint joinPoint) {
        String massage = createDate() + createMassageLogout(joinPoint);
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

    private String createMassageLogin(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        StandardSessionFacade session = (StandardSessionFacade) lArgs[0];
        User user = (User) session.getAttribute("user");
        return String.format("%-12sUser :  userLogin = %-15s go in",
                joinPoint.getSignature().getName(), user.getLogin());
    }

    private String createMassageLogout(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) lArgs[0];
        User user = (User) request.getSession().getAttribute("user");
        return String.format("%-10s  User :  userLogin = %-15s go out",
                joinPoint.getSignature().getName(), user.getLogin());
    }

    private String createDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS --- ");
        return dateFormat.format(date);
    }
}
