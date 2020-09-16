package ua.englishschool.backend.model.service.impl;

import org.springframework.stereotype.Service;
import ua.englishschool.backend.model.service.DateTimeService;

import java.time.LocalDateTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
