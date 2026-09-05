package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        log.info("Вызван метод createStudent");
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        log.info("Вызван метод getStudent");
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        log.info("Вызван метод getAllStudents");
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        log.info("Вызван метод updateStudent");
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long id) {
        log.info("Вызван метод deleteStudent");
        studentRepository.deleteById(id);
        return null;
    }

    public List<Student> findStudentsByAge(int age) {
        log.info("Вызван метод findStudentsByAge");
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() == age)
                .toList();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        log.info("Вызван метод findByAgeBetween");
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge)
                .toList();
    }

    public List<Student> findStudentsByName(String name) {
        log.info("Вызван метод findStudentsByName");
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Faculty getFacultyByStudent(Long studentId) {
        log.info("Вызван метод getFacultyByStudent");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return student.getFaculty();
    }

    public Student assignFacultyToStudent(Long studentId, Long facultyId) {
        log.info("Вызван метод assignFacultyToStudent");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public List<String> findNamesStartingWithA() {
        log.info("Вызван метод findNamesStartingWithA");
        return studentRepository.findByNameStartingWith("A").stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }

    public Double getAverageAge() {
        log.info("Вызван метод getAverageAge");
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}