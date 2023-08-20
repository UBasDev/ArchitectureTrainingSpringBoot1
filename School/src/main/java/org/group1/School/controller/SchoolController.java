package org.group1.School.controller;

import org.group1.School.client.StudentClient;
import org.group1.School.dto.FullSchoolResponseDto;
import org.group1.School.entity.School;
import org.group1.School.model.Student;
import org.group1.School.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/schools")
public class SchoolController {
    private final SchoolRepository schoolRepository;
    private final StudentClient studentClient;
    private final RestTemplate restTemplate;
    @Autowired
    public SchoolController(SchoolRepository schoolRepository, StudentClient studentClient, RestTemplate restTemplate) {
        this.schoolRepository = schoolRepository;
        this.studentClient = studentClient;
        this.restTemplate = restTemplate;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewSchool(@RequestBody School newSchool){
        this.schoolRepository.save(newSchool);
    }
    @GetMapping
    public ResponseEntity<List<School>> getAllSchools(){
        return ResponseEntity.status(200).body(this.schoolRepository.findAll());
    }
    @GetMapping(path = "withStudents/{schoolId}")
    public FullSchoolResponseDto getAllSchoolsBySchoolId(
            @PathVariable("schoolId") Integer schoolId
    ){
        var school = this.schoolRepository.findById(schoolId).orElse(new School());
        //var studentsFromMicroservice = this.studentClient.findAllStudentsBySchoolId(schoolId);
        var studentsFromMicroservice = this.restTemplate.exchange("http://STUDENTS/api/v1/students/school/" + schoolId.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
        var response = new FullSchoolResponseDto();
        response.setName(school.getName());
        response.setEmail(school.getEmail());
        response.setStudents(studentsFromMicroservice.getBody());
        return response;
    }
}
