package com.uep.sors.config;

import com.uep.sors.entity.RegistrationPeriod;
import com.uep.sors.repository.RegistrationPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RegistrationPeriodRepository registrationPeriodRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (registrationPeriodRepository.findAll().isEmpty()) {
            RegistrationPeriod period1 = RegistrationPeriod.builder()
                    .organizationId(1L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 5, 30, 23, 59))
                    .isActive(true)
                    .description("Computer Science Club - Spring 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period1);
            
            RegistrationPeriod period2 = RegistrationPeriod.builder()
                    .organizationId(2L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 5, 30, 23, 59))
                    .isActive(true)
                    .description("Engineering Club - Spring 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period2);
            
            RegistrationPeriod period3 = RegistrationPeriod.builder()
                    .organizationId(3L)
                    .startDate(LocalDateTime.of(2026, 4, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 4, 15, 23, 59))
                    .isActive(true)
                    .description("Music Club - Spring 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period3);
        }
    }
}
