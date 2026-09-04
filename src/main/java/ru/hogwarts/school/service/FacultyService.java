package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {
    private static final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        log.info("Вызван метод createFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        log.info("Вызван метод getFaculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public List<Faculty> getAllFaculties() {
        log.info("Вызван метод getAllFaculties");
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        log.info("Вызван метод updateFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(Long id) {
        log.info("Вызван метод deleteFaculty");
        facultyRepository.deleteById(id);
        return null;
    }

    public List<Faculty> findFacultyByName(String name) {
        log.info("Вызван метод findFacultyByName");
        return facultyRepository.findAll().stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Faculty> findFacultyByColor(String color) {
        log.info("Вызван метод findFacultyByColor");
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color))
                .toList();
    }

    public List<Faculty> searchFacultiesByNameOrColorIgnoreCase(String query) {
        log.info("Вызван метод searchFacultiesByNameOrColorIgnoreCase");
        String lowerCaseQuery = query.toLowerCase();
        return facultyRepository.findAll().stream().filter(f->f.getName().toLowerCase().contains(lowerCaseQuery)||
                f.getColor().toLowerCase().contains(lowerCaseQuery)).toList();
    }

    public List<Student> getStudentsByFaculty(Long facultyId) {
        log.info("Вызван метод getStudentsByFaculty");
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));
        return faculty.getStudents();
    }

    public String getLongestFacultyName() {
        log.info("Вызван метод getLongestFacultyName");
        return facultyRepository.findAll().stream()
                .max(Comparator.comparingInt(f -> f.getName().length()))
                .map(Faculty::getName)
                .orElse(null);
    }
}