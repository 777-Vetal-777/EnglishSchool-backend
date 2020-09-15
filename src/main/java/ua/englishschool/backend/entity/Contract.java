package ua.englishschool.backend.entity;

import ua.englishschool.backend.entity.core.ContractStatusType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "FK_students_contracts"))
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "FK_courses_contracts"))
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", foreignKey = @ForeignKey(name = "FK_teachers_contracts"))
    private Teacher teacher;

    @Column
    private Period period;

    @Column
    private Long money;

    @Column
    @Enumerated(EnumType.STRING)
    private ContractStatusType contractStatusType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id", foreignKey = @ForeignKey(name = "FK_student_invoices_contracts"))
    private List<StudentInvoice> studentInvoices = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public ContractStatusType getContractStatusType() {
        return contractStatusType;
    }

    public void setContractStatusType(ContractStatusType contractStatusType) {
        this.contractStatusType = contractStatusType;
    }

    public List<StudentInvoice> getStudentInvoices() {
        return studentInvoices;
    }

    public void setStudentInvoices(List<StudentInvoice> studentInvoices) {
        this.studentInvoices = studentInvoices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return id == contract.id &&
                Objects.equals(student, contract.student) &&
                Objects.equals(course, contract.course) &&
                Objects.equals(teacher, contract.teacher) &&
                Objects.equals(period, contract.period) &&
                Objects.equals(money, contract.money) &&
                contractStatusType == contract.contractStatusType &&
                Objects.equals(studentInvoices, contract.studentInvoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, course, teacher, period, money, contractStatusType, studentInvoices);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                ", teacher=" + teacher +
                ", period=" + period +
                ", money=" + money +
                ", contractStatusType=" + contractStatusType +
                ", studentInvoices=" + studentInvoices +
                '}';
    }
}
