package com.example.adaprivelearningnavigator.domain.quizAndProgress;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quiz_attempts", indexes = {
        @Index(name = "idx_attempt_user", columnList = "user_id"),
        @Index(name = "idx_attempt_quiz", columnList = "quiz_id")
})
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;
}
