package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    private String baseUrl;

    private Student testStudent;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        testFaculty = new Faculty(null, "Gryffindor", "Red");
        testFaculty = facultyService.createFaculty(testFaculty);

        testStudent = new Student("Harry Potter", 15);
        testStudent.setFaculty(testFaculty);
        testStudent = studentService.createStudent(testStudent);
    }

    @Test
    void testCreateStudent() {

        Student newStudent = new Student("Hermione Granger", 14);
        newStudent.setFaculty(testFaculty);


        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl + "/student",
                newStudent,
                Student.class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(newStudent.getName(), response.getBody().getName());
        assertEquals(newStudent.getAge(), response.getBody().getAge());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetStudent() {

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/student/{id}",
                Student.class,
                testStudent.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(testStudent.getName(), response.getBody().getName());
    }

    @Test
    void testUpdateStudent() {

        Student updatedStudent = new Student(
                testStudent.getId(),
                "Harry Potter Updated",
                16,
                testStudent.getFaculty()
        );

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(updatedStudent),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(updatedStudent.getName(), response.getBody().getName());
        assertEquals(updatedStudent.getAge(), response.getBody().getAge());
    }

    @Test
    void testDeleteStudent() {

        Long studentId = testStudent.getId();

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl + "/student/{id}",
                HttpMethod.DELETE,
                null,
                Student.class,
                studentId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());

        Student foundStudent = studentService.getStudent(studentId);
        assertNull(foundStudent);
    }

    @Test
    void testGetAllStudents() {

        Student anotherStudent = new Student("Ron Weasley", 15);
        anotherStudent.setFaculty(testFaculty);
        anotherStudent = studentService.createStudent(anotherStudent);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2); // минимум 2 студента (testStudent + anotherStudent)
    }

    @Test
    void testGetFacultyByStudent() {

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/student/{id}/faculty",
                Faculty.class,
                testStudent.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(testFaculty.getName(), response.getBody().getName());
        assertEquals(testFaculty.getColor(), response.getBody().getColor());
    }
}