package ua.englishschool.backend.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.dto.StudentDto;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.service.StudentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class StudentController {

    private static final String URL = "/students";

    private static final String URL_GET_STUDENT_DTO_BY_PHONE = URL + "/dto/{phone}";

    private static final String URL_GET_BY_ID = URL + "/{id}";

    private static final String URL_DELETE_BY_ID = URL + "/{id}";

    private static final String URL_FIND_ACTIVE_STUDENT_DTO = URL + "/active-students-dto";

    @Autowired
    private StudentService studentService;


    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long save(@RequestBody Student student) {
        return studentService.create(student).getId();
    }

    @DeleteMapping(URL_DELETE_BY_ID)
    public void deleteById(@PathVariable long id) {
        if (!studentService.delete(id)) {
            throw new EntityNotFoundException("Delete was failed for StudentId: " + id);
        }

    }

    @PutMapping(URL)
    public void update(@RequestBody Student student) {
        if (!studentService.update(student)) {
            throw new UpdateEntityException("Student was not updated for student id:" + student.getId());
        }
    }

    @GetMapping(URL_GET_BY_ID)
    public Student getById(@PathVariable("id") long id) {
        return studentService.getById(id).orElseThrow(() -> new EntityNotFoundException("Student was not found with id: " + id));
    }

    @GetMapping(URL)
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping(URL_GET_STUDENT_DTO_BY_PHONE)
    public StudentDto getStudentDtoByPhone(@PathVariable("phone") String phone) {
        return studentService.findStudentDtoByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Student was not found with phone: " + phone));
    }

    @GetMapping(URL_FIND_ACTIVE_STUDENT_DTO)
    public List<StudentDto> findActiveStudentsDto() {
        return studentService.findActiveStudentsDto();
    }
}
