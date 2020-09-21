package ua.englishschool.backend.model.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.PeriodDate;
import ua.englishschool.backend.entity.Teacher;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Course course;

    private Course course2;

    private PeriodDate periodDate;

    private PeriodDate periodDate2;

    private Teacher teacher;

    @BeforeEach
    void setUp() {

        teacher = new Teacher();
        teacher.setFirstName("Teacher");
        entityManager.persistAndFlush(teacher);

        periodDate = new PeriodDate();
        periodDate.setStartDate(LocalDate.parse("2022-09-22"));
        periodDate.setEndDate(LocalDate.parse("2022-10-21"));
        course = new Course();
        course.setPeriodDate(periodDate);
        course.setTeacher(teacher);
        entityManager.persistAndFlush(course);


        periodDate2 = new PeriodDate();
        periodDate2.setStartDate(LocalDate.parse("2020-09-20"));
        periodDate2.setEndDate(LocalDate.parse("2020-10-21"));
        course2 = new Course();
        course2.setPeriodDate(periodDate2);
        entityManager.persistAndFlush(course2);


    }

    @Test
    void whenFindAllByStartDateAfter_ReturnCourse() {

        List<Course> courses = courseRepository.findAllByStartDateAfter(LocalDate.now());

        assertEquals(Collections.singletonList(course), courses);
    }

    @Test
    void whenFindAllByStartDateAfter_ReturnCourses() {
        course2.getPeriodDate().setStartDate(LocalDate.parse("2020-09-27"));
        entityManager.merge(course2);
        List<Course> courses = courseRepository.findAllByStartDateAfter(LocalDate.now());

        assertEquals(Arrays.asList(course, course2), courses);

    }

    @Test
    void whenCountByTeacherAndEndDateAfter_ReturnCount() {

        int count = courseRepository.countByTeacherAndEndDateAfter(teacher, LocalDate.now());

        assertEquals(1, count);
    }
}
