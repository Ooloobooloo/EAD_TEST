package com.example.sis.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "student_score_t")
public class StudentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_score_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "score1", precision = 5, scale = 2)
    private BigDecimal score1;

    @Column(name = "score2", precision = 5, scale = 2)
    private BigDecimal score2;

    /** Grade = 0.3 * score1 + 0.7 * score2 */
    @Transient
    public double getFinalScore() {
        double s1 = score1 == null ? 0 : score1.doubleValue();
        double s2 = score2 == null ? 0 : score2.doubleValue();
        return 0.3 * s1 + 0.7 * s2;
    }

    /** 8.0-10: A | 6.0-7.9: B | 4.0-5.9: D | <4.0: F */
    @Transient
    public String getGrade() {
        double g = getFinalScore();
        if (g >= 8.0) return "A";
        if (g >= 6.0) return "B";
        if (g >= 4.0) return "D";
        return "F";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public BigDecimal getScore1() { return score1; }
    public void setScore1(BigDecimal score1) { this.score1 = score1; }
    public BigDecimal getScore2() { return score2; }
    public void setScore2(BigDecimal score2) { this.score2 = score2; }
}