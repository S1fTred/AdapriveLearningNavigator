package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.enums.QuizQuestionType;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quiz_questions", indexes = {
        @Index(name = "idx_question_quiz", columnList = "quiz_id")
})
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private QuizQuestionType type;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}
