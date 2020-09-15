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
import ua.englishschool.backend.entity.exception.DeleteEntityException;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.service.StudentService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class StudentController {

    private static final String URL = "/students";

    @Autowired
    private StudentService studentService;


    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long save(@RequestBody Student student) {
        return studentService.create(student).getId();
    }

    @DeleteMapping(URL + "/{id}")
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

    @GetMapping(URL + "/{id}")
    public Student getById(@PathVariable("id") long id) {
        return studentService.getById(id).orElseThrow(() -> new EntityNotFoundException("Student was not found with id: " + id));
    }

    @GetMapping(URL)
    public List<Student> getAll() {
        return studentService.getAll();
    }
}
