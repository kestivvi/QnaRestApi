package com.github.kestivvi.qna_rest_api.domain.question.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.github.kestivvi.qna_rest_api.domain.user.AppUser;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ANSWERS")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 101)
    private Long id;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 10000, message = "Field must be between {min} and {max} characters")
    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AppUser author;

    public String getBeginningOfTheMessage(int characters) {
        return this.getContent().length() <= characters ? this.getContent() : this.getContent().substring(0, characters);
    }
}
