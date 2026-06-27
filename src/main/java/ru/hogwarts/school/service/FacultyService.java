package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
   // @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
        return null;
    }

    public List<Faculty> findFacultyByName(String name) {
        return facultyRepository.findAll().stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Faculty> findFacultyByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color))
                .toList();
    }
}