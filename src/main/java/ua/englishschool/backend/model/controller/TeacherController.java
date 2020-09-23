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
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.core.RoleType;
import ua.englishschool.backend.entity.dto.TeacherDto;
import ua.englishschool.backend.entity.exception.UpdateEntityException;
import ua.englishschool.backend.model.service.TeacherService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
public class TeacherController {

    private static final String URL = "/teachers";

    private static final String URL_GET_ALL_DTO = URL + "/dto";

    private static final String URL_GET_BY_PHONE_DTO = URL + "/dto/by-phone/{phone}";

    private static final String URL_CHANGE_STATUS_ACTIVE = URL + "/change-active/{teacherId}";
    
    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(URL + "/{id}")
    public Teacher getById(@PathVariable("id") long id) {
        return teacherService.getById(id).orElseThrow(() -> new EntityNotFoundException("Teacher was not found with id: " + id));
    }

    @PostMapping(URL)
    @ResponseStatus(HttpStatus.CREATED)
    public long save(@RequestBody Teacher teacher) {
        teacher.setRole(RoleType.TEACHER);
        Optional<Teacher> teacherOptional = teacherService.findByPhone(teacher.getPhoneNumber());
        if (teacherOptional.isPresent()) {
          throw new EntityExistsException("Teacher is existed");
        }
        return teacherService.create(teacher).getId();
    }

    @PutMapping(URL)
    public void update(@RequestBody Teacher teacher) {
        if (!teacherService.update(teacher)) {
            throw new UpdateEntityException("Teacher was not updated for student id:" + teacher.getId());
        }
    }

    @DeleteMapping(URL + "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        if (!teacherService.delete(id)) {
            throw new EntityNotFoundException("Delete was failed for TeacherId: " + id);
        }
    }

    @GetMapping(URL)
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }

    @GetMapping(URL_GET_ALL_DTO)
    public List<TeacherDto> getAllDto() {
        return teacherService.getAllTeachersDto();
    }

    @GetMapping(URL_GET_BY_PHONE_DTO)
    public TeacherDto findByPhone(@PathVariable("phone") String phone) {
        return teacherService.findByPhoneDto(phone);
    }

    @PutMapping(URL_CHANGE_STATUS_ACTIVE)
    public boolean changeStatusActive(@PathVariable("teacherId") long id) {

        return teacherService.changeStatusActive(id);
    }
}
