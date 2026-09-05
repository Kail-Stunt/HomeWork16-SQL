package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByName(String name);
    List<Student> findByNameStartingWith(String prefix);

    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    Integer findAllStudents();

    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    Double findAvgAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFive();

}