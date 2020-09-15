package ua.englishschool.backend.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.exception.DeleteEntityException;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.service.TeacherService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class TeacherController {

    private static final String URL = "/teachers";

    @Autowired
    private TeacherService teacherService;

    @GetMapping(URL + "/{id}")
    public void getById(@PathVariable("id") long id) {
        teacherService.getById(id).orElseThrow(() -> new EntityNotFoundException("Teacher was not found with id: " + id));
    }

    @PostMapping(URL)
    public long save(Teacher teacher) {
        return teacherService.create(teacher).getId();
    }

    @PutMapping(URL)
    public void update(Teacher teacher) {
        if (!teacherService.update(teacher)) {
            throw new UpdateEntityException("Teacher was not updated for student id:" + teacher.getId());
        }
    }

    @DeleteMapping(URL + "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        if (!teacherService.delete(id)) {
            throw new DeleteEntityException("Delete was failed for TeacherId: " + id);
        }
    }

    @GetMapping(URL)
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }
}
