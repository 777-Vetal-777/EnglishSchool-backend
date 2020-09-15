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
import ua.englishschool.backend.model.service.StudentService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    private static final String URL = "/students";

    @Autowired
    private MockMvc server;


    @MockBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1);
        student.setFirstName("AAA");
        student.setLastName("BBB");
        student.setAddress("CCC");
        student.setPhoneNumber("12345");

    }

    @Test
    void whenCreateInvoked_thenReturnId() throws Exception {
        when(studentService.create(student)).thenReturn(student);

        server.perform(post(URL).content(asJsonString(student)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void deleteById_ReturnOK() throws Exception {
        when(studentService.delete(1)).thenReturn(true);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_IfNotFoundStudent() throws Exception {
        when(studentService.delete(1)).thenReturn(false);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_ReturnOK() throws Exception {
        when(studentService.update(student)).thenReturn(true);

        server.perform(put(URL).content(asJsonString(student)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
