package ua.englishschool.backend.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.Student;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.entity.dto.ContractDto;
import ua.englishschool.backend.entity.dto.CreateContractDto;
import ua.englishschool.backend.model.repository.ContractRepository;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.CourseService;
import ua.englishschool.backend.model.service.StudentService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    @Override
    public Contract create(Contract contract) {
        if (contract.getId() == 0) {
            return contractRepository.saveAndFlush(contract);
        }
        return contract;
    }

    @Override
    public long createContract(CreateContractDto createContractDto) {
        Optional<Course> course = courseService.getById(createContractDto.getCourseId());
        course.orElseThrow(() -> new EntityNotFoundException("Course was not found for id: " + createContractDto.getCourseId()));
        Optional<Student> student = studentService.getById(createContractDto.getStudentId());
        student.orElseThrow(() -> new EntityNotFoundException("Student was not found with id: " + createContractDto.getStudentId()));
        Contract contract = generateContract(student.get(), course.get());
        student.get().setActive(true);
        return create(contract).getId();
    }

    @Override
    public boolean update(Contract contract) {
        if (isExists(contract.getId())) {
            contractRepository.saveAndFlush(contract);
            return true;
        }
        return false;
    }

    @Override
    public List<Contract> getAll() {
        return contractRepository.findAll();
    }

    @Override
    public Optional<Contract> getById(long id) {
        return contractRepository.findById(id);
    }

    @Override
    public boolean delete(long id) {
        if (isExists(id)) {
            contractRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExists(long id) {
        return contractRepository.existsById(id);
    }

    @Override
    public List<Contract> getAllByStatus(ContractStatusType statusType) {
        return contractRepository.findAllByContractStatusType(statusType);

    }

    @Override
    public List<Contract> getAllByCourseAndStatusType(Course course, ContractStatusType statusType) {
        return contractRepository.findAllByCourseAndContractStatusType(course, statusType);
    }

    @Override
    public Optional<Contract> findContractByStudentAndContractStatusType(Student student, ContractStatusType contractStatusType) {
        return contractRepository.findContractByStudentAndContractStatusType(student, contractStatusType);
    }

    @Override
    public int countByContractStatusOpenAndWaitAndCourse(Course course) {
        return contractRepository.findCountByStatusOpenAndWaitAndCourse(course);
    }

    @Override
    public int countByStatusOpenAndCourse(Course course) {
        return contractRepository.findCountByCourseAndStatus(course, ContractStatusType.OPEN);
    }

    @Override
    public Optional<Contract> findByStudentAndStatusOpenOrWait(Student student) {
        return contractRepository.findByStudentAndStatusTypeOpenOrWait(student);
    }

    @Override
    public int countByCourseAndStatus(Course course, ContractStatusType type) {
        return contractRepository.findCountByCourseAndStatus(course, type);
    }

    @Override
    public ContractDto findByPhone(String phone) {
        Optional<Student> student = studentService.findStudentByPhone(phone);
        student.orElseThrow(() -> new EntityNotFoundException("Student was not found with phone: " + phone));
        Optional<Contract> contract = findByStudentAndStatusOpenOrWait(student.get());
        contract.orElseThrow(() -> new EntityNotFoundException("Contract was not found for student: " + student.get().toString()));

        return generateContractDto(contract.get(), student.get());
    }

    @Override
    public List<Contract> findAllByEndDateBefore(LocalDate localDate) {
        return contractRepository.findAllByEndDateBefore(localDate);
    }

    private ContractDto generateContractDto(Contract contract, Student student) {
        String teacherName = contract.getCourse().getTeacher().getFirstName().concat(" " + contract.getCourse().getTeacher().getLastName());
        ContractDto contractDto = new ContractDto();
        contractDto.setStudentName(student.getFirstName());
        contractDto.setStudentLastName(student.getLastName());
        contractDto.setCourseName(contract.getCourse().getName());
        contractDto.setTeacherName(teacherName);
        contractDto.setStartDate(contract.getCourse().getPeriodDate().getStartDate());
        contractDto.setEndDate(contract.getCourse().getPeriodDate().getEndDate());
        contractDto.setStartTime(contract.getCourse().getPeriodTime().getStartTime());
        contractDto.setEndTime(contract.getCourse().getPeriodTime().getEndTime());
        contractDto.setCourseId(contract.getCourse().getId());
        contractDto.setPhone(student.getPhoneNumber());
        return contractDto;
    }

    private Contract generateContract(Student student, Course course) {
        Contract contract = new Contract();
        contract.setCourse(course);
        contract.setStudent(student);
        if (course.getPeriodDate().getStartDate().isAfter(LocalDate.now())) {
            contract.setContractStatusType(ContractStatusType.WAIT);
        } else {
            contract.setContractStatusType(ContractStatusType.OPEN);
        }
        return contract;
    }
}
