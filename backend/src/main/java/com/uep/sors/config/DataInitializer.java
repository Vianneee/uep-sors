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
        // Initialize sample registration periods if they don't exist
        
        if (registrationPeriodRepository.findAll().isEmpty()) {
            // Sample organization 1 - Registration open
            RegistrationPeriod period1 = RegistrationPeriod.builder()
                    .organizationId(1L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 5, 30, 23, 59))
                    .isActive(true)
                    .description("Computer Science Club - Spring 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period1);
            
            // Sample organization 2 - Registration open
            RegistrationPeriod period2 = RegistrationPeriod.builder()
                    .organizationId(2L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 5, 30, 23, 59))
                    .isActive(true)
                    .description("Engineering Club - Spring 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period2);
            
            // Sample organization 3 - Registration closed (for demo)
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
            System.out.println("✓ Sample registration periods initialized successfully");
        }
    }
}
