package ua.englishschool.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("Select c from Course c where c.periodDate.startDate> :startDate")
    List<Course> findAllByStartDateAfter(@Param("startDate") LocalDate startDate);

    @Query("Select count(c) from Course c where c.teacher = :teacher and c.periodDate.endDate> :endDate")
    int countByTeacherAndEndDateAfter(@Param("teacher") Teacher teacher, @Param("endDate") LocalDate endDate);
}
