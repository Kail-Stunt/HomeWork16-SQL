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
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    private String baseUrl;

    private Faculty testFaculty;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        // Создаем тестовый факультет
        testFaculty = new Faculty(null, "Slytherin", "Green");
        testFaculty = facultyService.createFaculty(testFaculty);

        // Создаем тестового студента, привязанного к факультету
        testStudent = new Student("Draco Malfoy", 16);
        testStudent.setFaculty(testFaculty);
        testStudent = studentService.createStudent(testStudent);
    }

    @Test
    void testCreateFaculty() {

        Faculty newFaculty = new Faculty(null, "Ravenclaw", "Blue");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl + "/faculty",
                newFaculty,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(newFaculty.getName(), response.getBody().getName());
        assertEquals(newFaculty.getColor(), response.getBody().getColor());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetFaculty() {

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/faculty/{id}",
                Faculty.class,
                testFaculty.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(testFaculty.getName(), response.getBody().getName());
        assertEquals(testFaculty.getColor(), response.getBody().getColor());
    }

    @Test
    void testUpdateFaculty() {

        Faculty updatedFaculty = new Faculty(
                testFaculty.getId(),
                "Slytherin Updated",
                "Emerald"
        );

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(updatedFaculty),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(updatedFaculty.getName(), response.getBody().getName());
        assertEquals(updatedFaculty.getColor(), response.getBody().getColor());
    }

    @Test
    void testDeleteFaculty() {

        Long facultyId = testFaculty.getId();

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/faculty/{id}",
                HttpMethod.DELETE,
                null,
                Faculty.class,
                facultyId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());

        Faculty foundFaculty = facultyService.getFaculty(facultyId);
        assertNull(foundFaculty);
    }

    @Test
    void testGetAllFaculties() {

        Faculty anotherFaculty = new Faculty(null, "Hufflepuff", "Yellow");
        anotherFaculty = facultyService.createFaculty(anotherFaculty);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2); // минимум 2 факультета (testFaculty + anotherFaculty)
    }

    @Test
    void testGetStudentsByFaculty() {

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/faculty/{id}/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {},
                testFaculty.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testStudent.getName(), response.getBody().get(0).getName());
        assertEquals(testStudent.getAge(), response.getBody().get(0).getAge());
    }
}