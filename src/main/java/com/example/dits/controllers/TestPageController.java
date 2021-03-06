package com.example.dits.controllers;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.entity.*;
import com.example.dits.mapper.TestStatisticByUserMapper;
import com.example.dits.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class TestPageController {

    private final TopicService topicService;
    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final StatisticService statisticService;
    private final UserService userService;
    private final TestStatisticByUserMapper testStatisticByUserMapper;

    @GetMapping("/chooseTest")
    public String userPage(HttpSession session, ModelMap model) {
        List<Topic> topicList = topicService.findAll();
        List<Topic> topicsWithQuestions = new ArrayList<>();
        for (Topic i : topicList) {
            if (i.getTestList().size() != 0) {
                topicsWithQuestions.add(i);
            }
        }
        model.addAttribute("title", "Testing");
        model.addAttribute("topicWithQuestions", topicsWithQuestions);
        return "user/chooseTest";
    }

    @GetMapping("/goTest")
    public String goTest(@RequestParam int testId, @RequestParam(value = "theme") String topicName, ModelMap model, HttpSession session){
        Test test = testService.getTestByTestId(testId);
        List<Question> questionList = questionService.getQuestionsByTest(test);
        int quantityOfQuestions = questionList.size();
        int questionNumber = 0;
        int quantityOfRightAnswers = 0;

        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        session.setAttribute("testName", test.getName());
        session.setAttribute("topicName", topicName);
        session.setAttribute("questionSize", quantityOfQuestions);
        session.setAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        session.setAttribute("statistics", new ArrayList<Statistic>());
        session.setAttribute("questions",questionList);
        session.setAttribute("questionNumber" , ++questionNumber);

        model.addAttribute("question", questionDescription);
        model.addAttribute("answers", answers);
        model.addAttribute("title","Testing");
        return "user/testPage";
    }

    @GetMapping("/nextTestPage")
    public String nextTestPage(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                               ModelMap model,
                               HttpSession session){

        List<Question> questionList = (List<Question>) session.getAttribute("questions");
        int questionNumber = (int) session.getAttribute("questionNumber");
        User user = (User) session.getAttribute("user");
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion,questionList,questionNumber);

        List<Answer> answers = answerService.getAnswersFromQuestionList(questionList, questionNumber);
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        statisticList.add(Statistic.builder()
                .question(questionList.get(questionNumber-1))
                .user(user)
                .correct(isCorrect).build());

        session.setAttribute("statistics", statisticList);
        session.setAttribute("questionNumber" , ++questionNumber);
        model.addAttribute("question",questionDescription);
        model.addAttribute("answers", answers);
        model.addAttribute("title","Testing");
        return "user/testPage";
    }

    @GetMapping("/resultPage")
    public String testStatistic(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                                ModelMap model,
                                HttpSession session){


        List<Question> questions = (List<Question>) session.getAttribute("questions");
        int questionNumber = questions.size();
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion,questions,questionNumber);
        User user = (User) session.getAttribute("user");
        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");

        checkIfResultPage(questions, questionNumber, isCorrect, user, statisticList);
        statisticService.saveStatisticsToDB(statisticList);
        int correctAnswers = (int) statisticList.stream().filter(answer -> answer.isCorrect()==true).count();
        int sumAnswers = statisticList.size();
        int wrongAnswers = sumAnswers - correctAnswers;
        int resultPercent = 100 * correctAnswers / sumAnswers;
        model.addAttribute("title", "Result");
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("wrongAnswers", wrongAnswers);
        model.addAttribute("resultPercent", resultPercent);
        return "user/resultPage";
    }

    @GetMapping("/personalStatistic")
    public String personalStatistic(ModelMap model, HttpSession session ){
        User user = (User) session.getAttribute("user");
        model.addAttribute("title","Statistics");
        model.addAttribute("allStatistic",userTestsStatisticDTO(user.getUserId()));
        return "user/personalStatistic";
    }

    @ResponseBody
    @GetMapping("/getUserTestsStatistic")
    public List<TestStatisticByUser> getUserTestsStatistic(HttpSession session ){
        User user = (User) session.getAttribute("user");
        return userTestsStatisticDTO(user.getUserId());
    }

    private void checkIfResultPage(List<Question> questions, int questionNumber, boolean isCorrect, User user, List<Statistic> statisticList) {
        if (!isResultPage(questionNumber, statisticList)){
            statisticList.add(Statistic.builder()
                    .question(questions.get(questionNumber -1))
                    .user(user)
                    .correct(isCorrect).build());
        }
    }

    private boolean isResultPage(int questionNumber, List<Statistic> statisticList) {
        return statisticList.size() >= questionNumber;
    }

    private List<TestStatisticByUser> userTestsStatisticDTO(int id){
        User user = userService.getUserByUserId(id);
        List<Statistic> testList = statisticService.getStatisticsByUser(user);
        return testStatisticByUserMapper.convertListToTestStatisticByUser(testList);
    }
}