package ua.englishschool.backend.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.englishschool.backend.entity.Contract;
import ua.englishschool.backend.entity.Course;
import ua.englishschool.backend.entity.core.ContractStatusType;
import ua.englishschool.backend.model.service.ContractService;
import ua.englishschool.backend.model.service.CourseService;
import ua.englishschool.backend.model.service.DateTimeService;

import java.sql.Timestamp;
import java.util.List;


@Component
public class ContractWorker {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private DateTimeService dateTimeService;

    @Scheduled(cron = "${close.contracts}")
    public void closeContracts() {
        List<Course> listCourses = courseService.getAll();
        for (Course course : listCourses) {
            if (course.getPeriod().getEndDate().before(Timestamp.valueOf(dateTimeService.now()))) {
                closeContracts(contractService.getAllByCourseAndStatusType(course, ContractStatusType.OPEN));
            }
        }


    }

    private void closeContracts(List<Contract> contractList) {
        for (Contract contract : contractList) {
            contract.setContractStatusType(ContractStatusType.CLOSED);
        }
    }


}
