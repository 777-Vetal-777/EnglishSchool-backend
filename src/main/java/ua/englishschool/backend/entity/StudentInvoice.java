package ua.englishschool.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class StudentInvoice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Period period;

    @Column(name = "payment_date")
    private Timestamp paymentDate;

    @Column
    private Long money;

    @Column
    private boolean payed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentInvoice that = (StudentInvoice) o;
        return id == that.id &&
                payed == that.payed &&
                Objects.equals(period, that.period) &&
                Objects.equals(paymentDate, that.paymentDate) &&
                Objects.equals(money, that.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, paymentDate, money, payed);
    }

    @Override
    public String toString() {
        return "StudentInvoice{" +
                "id=" + id +
                ", period=" + period +
                ", paymentDate=" + paymentDate +
                ", money=" + money +
                ", payed=" + payed +
                '}';
    }
}
