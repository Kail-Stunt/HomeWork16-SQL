package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private StudentService studentService;

    private Faculty testFaculty;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty(1L, "Slytherin", "Green");
        testStudent = new Student(1L, "Draco Malfoy", 16, testFaculty);
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty newFaculty = new Faculty(null, "Ravenclaw", "Blue");

        when(facultyService.createFaculty(any(Faculty.class)))
                .thenReturn(newFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Ravenclaw")))
                .andExpect(jsonPath("$.color", is("Blue")));
    }

    @Test
    void testGetFaculty() throws Exception {
        when(facultyService.getFaculty(testFaculty.getId()))
                .thenReturn(testFaculty);

        mockMvc.perform(get("/faculty/{id}", testFaculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Slytherin")))
                .andExpect(jsonPath("$.color", is("Green")));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty(1L, "Slytherin Updated", "Emerald");
        when(facultyService.updateFaculty(any(Faculty.class)))
                .thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Slytherin Updated")))
                .andExpect(jsonPath("$.color", is("Emerald")));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        when(facultyService.deleteFaculty(testFaculty.getId()))
                .thenReturn(testFaculty);

        mockMvc.perform(delete("/faculty/{id}", testFaculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Slytherin")));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty anotherFaculty = new Faculty(2L, "Hufflepuff", "Yellow");
        when(facultyService.getAllFaculties())
                .thenReturn(List.of(testFaculty, anotherFaculty));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Slytherin")))
                .andExpect(jsonPath("$[1].name", is("Hufflepuff")));
    }

    @Test
    void testGetStudentsByFaculty() throws Exception {
        when(facultyService.getStudentsByFaculty(testFaculty.getId()))
                .thenReturn(List.of(testStudent));

        mockMvc.perform(get("/faculty/{id}/students", testFaculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is("Draco Malfoy")))
                .andExpect(jsonPath("$[0].age", is(16)));
    }
}