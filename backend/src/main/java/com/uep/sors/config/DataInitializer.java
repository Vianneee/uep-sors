package com.uep.sors.config;
 
import com.uep.sors.entity.Role;
import com.uep.sors.entity.User;
import com.uep.sors.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 
    @Override
    public void run(String... args) throws Exception {
        if (registrationPeriodRepository.findAll().isEmpty()) {
            RegistrationPeriod period1 = RegistrationPeriod.builder()
                    .organizationId(1L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 12, 31, 23, 59))
                    .isActive(true)
                    .description("Computer Science Club - 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period1);
 
            RegistrationPeriod period2 = RegistrationPeriod.builder()
                    .organizationId(2L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 12, 31, 23, 59))
                    .isActive(true)
                    .description("Engineering Club - 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period2);
 
            RegistrationPeriod period3 = RegistrationPeriod.builder()
                    .organizationId(3L)
                    .startDate(LocalDateTime.of(2026, 5, 1, 0, 0))
                    .endDate(LocalDateTime.of(2026, 12, 31, 23, 59))
                    .isActive(true)
                    .description("Music Club - 2026 Recruitment")
                    .build();
            registrationPeriodRepository.save(period3);

            for (long i = 4; i <= 12; i++) {
                RegistrationPeriod p = RegistrationPeriod.builder()
                        .organizationId(i)
                        .startDate(LocalDateTime.of(2026, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2026, 12, 31, 23, 59))
                        .isActive(true)
                        .description("Organization " + i + " - 2026 Recruitment")
                        .build();
                registrationPeriodRepository.save(p);
            }
        }
 
        if (userRepository.findByEmail("admin@uep.edu.ph").isEmpty()) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setStudentId("000000");
            admin.setAge(30);
            admin.setProgram("Administration");
            admin.setYearLevel(1);
            admin.setEmail("admin@uep.edu.ph");
            admin.setPassword(passwordEncoder.encode("Admin@1234"));
            admin.setRole(Role.ADMIN);
            admin.setIsVerified(true);
            userRepository.save(admin);
        }
 
        if (userRepository.findByEmail("editorinchief@uep.edu.ph").isEmpty()) {
            User eic = new User();
            eic.setFullName("Editor in Chief");
            eic.setStudentId("000001");
            eic.setAge(25);
            eic.setProgram("Administration");
            eic.setYearLevel(1);
            eic.setEmail("editorinchief@uep.edu.ph");
            eic.setPassword(passwordEncoder.encode("Editor@1234"));
            eic.setRole(Role.EDITOR_IN_CHIEF);
            eic.setIsVerified(true);
            userRepository.save(eic);
        }
    }
}