package com.uep.sors.config;
 
import com.uep.sors.entity.Organization;
import com.uep.sors.entity.Role;
import com.uep.sors.entity.User;
import com.uep.sors.repository.OrganizationRepository;
import com.uep.sors.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.uep.sors.entity.RegistrationPeriod;
import com.uep.sors.repository.RegistrationPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
 
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
 
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;
 
    @Override
    public void run(String... args) throws Exception {
        seedOrganizations();
        seedRegistrationPeriods();
        seedUsers();
    }

    private void seedOrganizations() {
        if (organizationRepository.count() == 0) {
            organizationRepository.saveAll(List.of(
                    createOrg("Balangaw", "A student publication organization.", "Non-Academic",
                            "imgs/balangaw.jpg", Arrays.asList("Editor: Juan Dela Cruz", "PIO: Maria Santos")),
                    createOrg("University Performing Arts Organization (UPAO)", "Promotes arts and culture.", "Non-Academic",
                            "imgs/upao.jpg", Arrays.asList("President: Ana Reyes", "VP: Carlo Mendoza")),
                    createOrg("Campus Ministry Organization (CMO)", "Faith-based student organization.", "Non-Academic",
                            "imgs/cmo.jpg", Arrays.asList("President: Rose Bautista")),
                    createOrg("Red Cross Youth", "Humanitarian and civic organization.", "Civic",
                            "imgs/redcross.jpg", Arrays.asList("President: Mark Lopez")),
                    createOrg("Eagle Scout Organization of Northern Samar (ESANAS)", "Scouting organization.", "Civic",
                            "imgs/esans.jpg", Arrays.asList("President: Jose Ramos")),
                    createOrg("Bayanihan Youth For Peace", "Peace advocacy organization.", "Civic",
                            "imgs/bayanihan.png", Arrays.asList("President: Liza Cruz")),
                    createOrg("Upsilon Phi Sigma", "Academic fraternity.", "Academic",
                            "imgs/upsilon.jpg", Arrays.asList("President: Ryan Tan")),
                    createOrg("CFC-Youth for Christ", "Christian civic organization.", "Civic",
                            "imgs/cfc.jpg", Arrays.asList("President: Grace Villanueva")),
                    createOrg("Leyte Samar Student Organization (LEYSAM)", "Regional student organization.", "Non-Academic",
                            "imgs/leysam.jpg", Arrays.asList("President: Paolo Mercado")),
                    createOrg("Sinag ng Hilaga", "Cultural organization.", "Non-Academic",
                            "imgs/sanag.jpg", Arrays.asList("President: Nina Castillo")),
                    createOrg("Kappa Nu Gamma", "Academic sorority.", "Academic",
                            "imgs/kappa.jpg", Arrays.asList("President: Sophia dela Rosa")),
                    createOrg("Physical Education Student Association (PESA)", "PE student organization.", "Academic",
                            "imgs/pesa.jpg", Arrays.asList("President: Marco Aquino"))
            ));
        }
    }

    private Organization createOrg(String name, String desc, String category, String logo, List<String> officers) {
        Organization o = new Organization();
        o.setName(name);
        o.setDescription(desc);
        o.setCategory(category);
        o.setLogoUrl(logo);
        o.setOfficers(officers);
        return o;
    }

    private void seedRegistrationPeriods() {
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
    }
 
    private void seedUsers() {
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
