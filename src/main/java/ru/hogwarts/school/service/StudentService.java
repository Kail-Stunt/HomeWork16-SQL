package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class StudentService {
    //@Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
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

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge)
                .toList();
    }

    public List<Student> findStudentsByName(String name) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .toList();
    }

    /*public Faculty getFacultyByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return student.getFaculty();
    }*/

    public Faculty getFacultyByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return student.getFaculty();  //Hibernate Lazy Loading сработает здесь
    }

    public Student assignFacultyToStudent(Long studentId, Long facultyId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        student.setFaculty(faculty);
        return studentRepository.save(student);
    }
}