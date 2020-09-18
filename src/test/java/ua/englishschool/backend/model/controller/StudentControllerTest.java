package ua.englishschool.backend.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.dto.StudentDto;
import ua.englishschool.backend.model.service.StudentService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    private static final String URL = "/students";

    private static final String URL_GET_STUDENT_DTO_BY_PHONE = URL + "/dto/{phone}";

    private static final String URL_FIND_ACTIVE_STUDENT_DTO = URL + "/active-students-dto";

    private static final String URL_GET_ALL_STUDENTS_DTO = URL + "/get-all-dto";

    private static final long STUDENT_ID = 1;

    private static final String PHONE = "7777777777";

    @Autowired
    private MockMvc server;

    @MockBean
    private StudentService studentService;

    private Student student;

    private StudentDto studentDto;


    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName("firstName");
        student.setLastName("BBB");
        student.setAddress("lastName");
        student.setPhoneNumber(PHONE);

        studentDto = new StudentDto();
        studentDto.setFirstName("firstName");
        studentDto.setLastName("lastName");
    }

    @Test
    void create_ReturnId() throws Exception {
        when(studentService.create(student)).thenReturn(student);

        server.perform(post(URL).content(asJsonString(student))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(studentService).create(student);

    }

    @Test
    void deleteById_ReturnOK() throws Exception {
        when(studentService.delete(STUDENT_ID)).thenReturn(true);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(studentService).delete(STUDENT_ID);

    }

    @Test
    void deleteById_IfNotFoundStudent() throws Exception {
        when(studentService.delete(STUDENT_ID)).thenReturn(false);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(STUDENT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(studentService).delete(STUDENT_ID);
    }

    @Test
    void update_ReturnOK() throws Exception {
        when(studentService.update(student)).thenReturn(true);

        server.perform(put(URL).content(asJsonString(student))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(studentService).update(student);
    }

    @Test
    void update_IfNotUpdatedStudent() throws Exception {
        when(studentService.update(student)).thenReturn(false);

        server.perform(put(URL).content(asJsonString(student))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(studentService).update(student);

    }

    @Test
    void getById_ReturnStudent() throws Exception {
        when(studentService.getById(STUDENT_ID)).thenReturn(Optional.ofNullable(student));

        server.perform(get(URL.concat("/{id}"), String.valueOf(STUDENT_ID)))
                .andDo(print())
                .andExpect(content().json(asJsonString(student)))
                .andExpect(status().isOk());

        verify(studentService).getById(STUDENT_ID);

    }

    @Test
    void getById_IfNotFoundStudent() throws Exception {
        when(studentService.getById(STUDENT_ID)).thenReturn(Optional.empty());

        server.perform(get(URL.concat("/{id}"), String.valueOf(STUDENT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(studentService).getById(STUDENT_ID);

    }

    @Test
    void getAll_ReturnListStudents() throws Exception {
        when(studentService.getAll()).thenReturn(Collections.singletonList(student));

        server.perform(get(URL))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singleton(student))))
                .andExpect(status().isOk());

        verify(studentService).getAll();

    }

    @Test
    void getStudentByPhone_ReturnStudentDto() throws Exception {
        when(studentService.findStudentDtoByPhone(PHONE)).thenReturn(Optional.ofNullable(studentDto));

        server.perform(get(URL_GET_STUDENT_DTO_BY_PHONE, PHONE))
                .andDo(print())
                .andExpect(content().json(asJsonString(studentDto)))
                .andExpect(status().isOk());

        verify(studentService).findStudentDtoByPhone(PHONE);
    }

    @Test
    void getStudentByPhone_IfNotFoundStudent() throws Exception {
        when(studentService.findStudentDtoByPhone(PHONE)).thenReturn(Optional.empty());

        server.perform(get(URL_GET_STUDENT_DTO_BY_PHONE, PHONE))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(studentService).findStudentDtoByPhone(PHONE);

    }

    @Test
    void findActiveStudents_ReturnListStudents() throws Exception {
        when(studentService.findActiveStudentsDto()).thenReturn(Collections.singletonList(studentDto));

        server.perform(get(URL_FIND_ACTIVE_STUDENT_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(studentDto))))
                .andExpect(status().isOk());

        verify(studentService).findActiveStudentsDto();
    }

    @Test
    void findAllStudentsDto_ReturnListStudents() throws Exception {
        when(studentService.findAllStudentsDto()).thenReturn(Collections.singletonList(studentDto));

        server.perform(get(URL_GET_ALL_STUDENTS_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(studentDto))))
                .andExpect(status().isOk());

        verify(studentService).findAllStudentsDto();
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
