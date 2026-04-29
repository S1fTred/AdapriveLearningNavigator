package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.QuizService;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizAttemptResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizQuestionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizSubmitRequest;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/topics/{topicId}/quiz")
    public QuizResponse getTopicQuiz(@PathVariable Long topicId,
                                     Authentication authentication) {
        requireUserId(authentication);
        return quizService.getTopicQuiz(topicId);
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public List<QuizQuestionResponse> getQuizQuestions(@PathVariable Long quizId,
                                                       Authentication authentication) {
        requireUserId(authentication);
        return quizService.getQuizQuestions(quizId);
    }

    @PostMapping("/quizzes/attempts")
    public QuizAttemptResponse submitQuiz(@Valid @RequestBody QuizSubmitRequest request,
                                          Authentication authentication) {
        return quizService.submitQuiz(requireUserId(authentication), request);
    }

    @GetMapping("/quizzes/attempts")
    public List<QuizAttemptResponse> getAttempts(@RequestParam(required = false) Long quizId,
                                                 Authentication authentication) {
        return quizService.getAttempts(requireUserId(authentication), quizId);
    }

    private Long requireUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthException("Требуется авторизация");
        }
        return principal.getUserId();
    }
}
