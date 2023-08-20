package org.group1.Student.controller;

import org.group1.Student.entity.Student;
import org.group1.Student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/students")
public class StudentController {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewStudent(@RequestBody Student newStudent){
    this.studentRepository.save(newStudent);
    }
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){
        return ResponseEntity.status(200).body(this.studentRepository.findAll());
    }
    @GetMapping(path = "/school/{schoolId}")
    public ResponseEntity<List<Student>> getAllStudents(
            @PathVariable("schoolId") Integer schoolId
    ){
        System.out.println("Mikroservis, iletişime geçti!");
        return ResponseEntity.status(200).body(this.studentRepository.findAllBySchoolId(schoolId));
    }
}
