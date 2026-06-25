package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    //@Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long id) {
        studentRepository.deleteById(id);
        return null;
    }

    public List<Student> findStudentsByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() == age)
                .toList();
    }

    public List<Student> findStudentsByAgeRange(int minAge, int maxAge) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge)
                .toList();
    }

    public List<Student> findStudentsByName(String name) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .toList();
    }
}