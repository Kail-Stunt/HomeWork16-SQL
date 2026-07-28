package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student testStudent;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty(1L, "Gryffindor", "Red");
        testStudent = new Student(1L, "Harry Potter", 15, testFaculty);
    }

    @Test
    void testCreateStudent() throws Exception {
        Student newStudent = new Student("Hermione Granger", 14);
        newStudent.setFaculty(testFaculty);

        when(studentService.createStudent(any(Student.class)))
                .thenReturn(newStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Hermione Granger")))
                .andExpect(jsonPath("$.age", is(14)));
    }

    @Test
    void testGetStudent() throws Exception {
        when(studentService.getStudent(testStudent.getId()))
                .thenReturn(testStudent);

        mockMvc.perform(get("/student/{id}", testStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Harry Potter")))
                .andExpect(jsonPath("$.age", is(15)));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1L, "Harry Potter Updated", 16, testFaculty);
        when(studentService.updateStudent(any(Student.class)))
                .thenReturn(updatedStudent);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Harry Potter Updated")))
                .andExpect(jsonPath("$.age", is(16)));
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.deleteStudent(testStudent.getId()))
                .thenReturn(testStudent);

        mockMvc.perform(delete("/student/{id}", testStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Harry Potter")));
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student anotherStudent = new Student(2L, "Ron Weasley", 15, testFaculty);
        when(studentService.getAllStudents())
                .thenReturn(List.of(testStudent, anotherStudent));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Harry Potter")))
                .andExpect(jsonPath("$[1].name", is("Ron Weasley")));
    }

    @Test
    void testGetFacultyByStudent() throws Exception {
        when(studentService.getFacultyByStudent(testStudent.getId()))
                .thenReturn(testFaculty);

        mockMvc.perform(get("/student/{id}/faculty", testStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Gryffindor")))
                .andExpect(jsonPath("$.color", is("Red")));
    }
}