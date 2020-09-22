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
import ua.englishschool.backend.entity.Teacher;
import ua.englishschool.backend.entity.core.RoleType;
import ua.englishschool.backend.entity.dto.TeacherDto;
import ua.englishschool.backend.model.service.TeacherService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    private static final String URL = "/teachers";

    private static final String URL_GET_ALL_DTO = URL + "/dto";

    private static final String URL_GET_BY_PHONE_DTO = URL + "/dto/by-phone/{phone}";

    private static final long TEACHER_ID = 1;

    private static final String PHONE = "12345";

    @Autowired
    private MockMvc server;

    @MockBean
    private TeacherService teacherService;

    private Teacher teacher;

    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setRole(RoleType.TEACHER);

        teacherDto = new TeacherDto();
        teacherDto.setFirstName("teacherName");
    }

    @Test
    void create_ReturnId() throws Exception {
        when(teacherService.create(teacher)).thenReturn(teacher);

        server.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(teacher)))
                .andDo(print())
                .andExpect(content().string(String.valueOf(TEACHER_ID)))
                .andExpect(status().isCreated());

        verify(teacherService, times(1)).create(teacher);

    }

    @Test
    void getById_ReturnOK() throws Exception {
        when(teacherService.getById(TEACHER_ID)).thenReturn(Optional.ofNullable(teacher));

        server.perform(get(URL.concat("/{id}"), String.valueOf(TEACHER_ID)))
                .andDo(print())
                .andExpect(content().json(asJsonString(teacher)))
                .andExpect(status().isOk());

        verify(teacherService).getById(TEACHER_ID);
    }

    @Test
    void getById_IfNotFoundTeacher() throws Exception {
        when(teacherService.getById(TEACHER_ID)).thenReturn(Optional.empty());

        server.perform(get(URL.concat("/{id}"), String.valueOf(TEACHER_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(teacherService).getById(TEACHER_ID);

    }

    @Test
    void update_ReturnOK() throws Exception {
        when(teacherService.update(teacher)).thenReturn(true);

        server.perform(put(URL).content(asJsonString(teacher))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(teacherService).update(teacher);
    }

    @Test
    void update_IfNotUpdatedTeacher() throws Exception {
        when(teacherService.update(teacher)).thenReturn(false);

        server.perform(put(URL).content(asJsonString(teacher))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(teacherService).update(teacher);

    }


    @Test
    void getAll_ReturnListStudents() throws Exception {
        when(teacherService.getAll()).thenReturn(Collections.singletonList(teacher));

        server.perform(get(URL))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singleton(teacher))))
                .andExpect(status().isOk());

        verify(teacherService).getAll();

    }

    @Test
    void deleteById_ReturnOK() throws Exception {
        when(teacherService.delete(TEACHER_ID)).thenReturn(true);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(teacherService).delete(TEACHER_ID);

    }

    @Test
    void deleteById_IfNotFoundTeacher() throws Exception {
        when(teacherService.delete(TEACHER_ID)).thenReturn(false);

        server.perform(delete(URL.concat("/{id}"), String.valueOf(TEACHER_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(teacherService).delete(TEACHER_ID);
    }

    @Test
    void getAllDto_ReturnOk() throws Exception {
        when(teacherService.getAllTeachersDto()).thenReturn(Collections.singletonList(teacherDto));

        server.perform(get(URL_GET_ALL_DTO))
                .andDo(print())
                .andExpect(content().json(asJsonString(Collections.singletonList(teacherDto))))
                .andExpect(status().isOk());
    }

    @Test
    void findByPhone_ReturnOk() throws Exception {
        when(teacherService.findByPhoneDto(PHONE)).thenReturn(teacherDto);

        server.perform(get(URL_GET_BY_PHONE_DTO,PHONE))
                .andDo(print())
                .andExpect(content().json(asJsonString(teacherDto)))
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
