package ua.englishschool.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StudentDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
