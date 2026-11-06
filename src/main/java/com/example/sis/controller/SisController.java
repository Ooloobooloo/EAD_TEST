package com.example.sis.controller;

import com.example.sis.entity.Student;
import com.example.sis.entity.StudentScore;
import com.example.sis.repository.StudentRepository;
import com.example.sis.repository.StudentScoreRepository;
import com.example.sis.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SisController {

    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final StudentScoreRepository scoreRepo;

    public SisController(StudentRepository studentRepo,
                         SubjectRepository subjectRepo,
                         StudentScoreRepository scoreRepo) {
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.scoreRepo = scoreRepo;
    }

    // ===== Question 3: display =====
    @GetMapping({"/", "/students"})
    public String index(Model model) {
        model.addAttribute("scores", scoreRepo.findAllByOrderByIdAsc());
        return "index";
    }

    // ===== Question 1: insert student =====
    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping("/students")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "student-form";
        }
        studentRepo.save(student);
        return "redirect:/";
    }

    // ===== Question 2: insert score =====
    @GetMapping("/scores/new")
    public String newScore(Model model) {
        model.addAttribute("scoreForm", new ScoreForm());
        loadCombos(model);
        return "score-form";
    }

    @PostMapping("/scores")
    public String saveScore(@Valid @ModelAttribute("scoreForm") ScoreForm form,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            loadCombos(model);
            return "score-form";
        }
        StudentScore sc = new StudentScore();
        sc.setStudent(studentRepo.findById(form.getStudentId()).orElseThrow());
        sc.setSubject(subjectRepo.findById(form.getSubjectId()).orElseThrow());
        sc.setScore1(form.getScore1());
        sc.setScore2(form.getScore2());
        scoreRepo.save(sc);
        return "redirect:/";
    }

    private void loadCombos(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        model.addAttribute("subjects", subjectRepo.findAll());
    }
}