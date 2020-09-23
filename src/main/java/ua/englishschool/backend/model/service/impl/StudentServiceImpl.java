package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.StudentDto;
import ua.englishschool.backend.model.repository.StudentRepository;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.StudentService;
import ua.englishschool.backend.model.service.TeacherService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    private ContractService contractService;

    private TeacherService teacherService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              ContractService contractService, TeacherService teacherService) {
        this.studentRepository = studentRepository;
        this.contractService = contractService;
        this.teacherService = teacherService;
    }

    @Override
    public Student create(Student student) {
        if (findStudentByPhone(student.getPhoneNumber()).isPresent()) {
            throw new EntityExistsException("Student with this phone is already in database: " + student.toString());
        }
        if (teacherService.findByPhone(student.getPhoneNumber()).isPresent()) {
            throw new EntityNotFoundException("Teacher with this phone is already in database: " + student.getPhoneNumber());
        }
        if (student.getId() == 0) {
            return studentRepository.saveAndFlush(student);
        }
        return student;
    }

    @Override
    public boolean update(Student student) {
        if (isExists(student.getId())) {
            studentRepository.saveAndFlush(student);
            return true;
        }
        return false;
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getById(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Optional<Student> findStudentByPhone(String phone) {
        return studentRepository.findByPhoneNumber(phone);
    }


    public Optional<StudentDto> findStudentDtoByPhone(String phone) {
        Optional<Student> student = studentRepository.findByPhoneNumber(phone);
        if (student.isEmpty()) {
            throw new EntityNotFoundException("Student was not found with this phone");
        }
        Optional<Contract> contract = contractService.findContractByStudentAndContractStatusType(student.get(), ContractStatusType.OPEN);
        if (contract.isEmpty()) {
            return Optional.of(new StudentDto(student.get().getId(), student.get().getFirstName(),
                    student.get().getLastName(), student.get().getPhoneNumber(), null));
        }

        return Optional.of(new StudentDto(student.get().getId(), student.get().getFirstName(), student.get().getLastName(),
                student.get().getPhoneNumber(), contract.get().getCourse().getName()));
    }


    @Override
    public List<StudentDto> findActiveStudentsDto() {

        return contractService.getAllByStatus(ContractStatusType.OPEN).stream()
                .map(contract -> new StudentDto(contract.getStudent().getId(), contract.getStudent().getFirstName(), contract.getStudent().getLastName(),
                        contract.getStudent().getPhoneNumber(), contract.getCourse().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> findAllStudentsDto() {
        List<StudentDto> list = studentRepository.findAllByActive(true).stream()
                .map(student -> generateStudentDto(student))
                .collect(Collectors.toList());
        list.addAll(findAllByActiveFalseDto());
        return list;
    }

    @Override
    public List<StudentDto> findAllByActiveFalseDto() {
        return studentRepository.findAllByActive(false).stream()
                .map(student -> generateStudentDto(student))
                .collect(Collectors.toList());
    }

    private StudentDto generateStudentDto(Student student) {
        Optional<Contract> contract = contractService.findByStudentAndStatusOpenOrWait(student);
        String courseName = null;
        if (contract.isPresent()) {
            courseName = contract.get().getCourse().getName();
        }
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setPhoneNumber(student.getPhoneNumber());
        studentDto.setCourseName(courseName);
        return studentDto;
    }
}
