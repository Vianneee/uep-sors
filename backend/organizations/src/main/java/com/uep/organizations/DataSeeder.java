package com.uep.organizations;

import com.uep.organizations.model.Organization;
import com.uep.organizations.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(OrganizationRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                        create("Balangaw", "A student publication organization.", "Non-Academic",
                                "imgs/balangaw.jpg", Arrays.asList("Editor: Juan Dela Cruz", "PIO: Maria Santos")),
                        create("University Performing Arts Organization (UPAO)", "Promotes arts and culture.", "Non-Academic",
                                "imgs/upao.jpg", Arrays.asList("President: Ana Reyes", "VP: Carlo Mendoza")),
                        create("Campus Ministry Organization (CMO)", "Faith-based student organization.", "Non-Academic",
                                "imgs/cmo.jpg", Arrays.asList("President: Rose Bautista")),
                        create("Red Cross Youth", "Humanitarian and civic organization.", "Civic",
                                "imgs/redcross.jpg", Arrays.asList("President: Mark Lopez")),
                        create("Eagle Scout Organization of Northern Samar (ESANAS)", "Scouting organization.", "Civic",
                                "imgs/esans.jpg", Arrays.asList("President: Jose Ramos")),
                        create("Bayanihan Youth For Peace", "Peace advocacy organization.", "Civic",
                                "imgs/bayanihan.png", Arrays.asList("President: Liza Cruz")),
                        create("Upsilon Phi Sigma", "Academic fraternity.", "Academic",
                                "imgs/upsilon.jpg", Arrays.asList("President: Ryan Tan")),
                        create("CFC-Youth for Christ", "Christian civic organization.", "Civic",
                                "imgs/cfc.jpg", Arrays.asList("President: Grace Villanueva")),
                        create("Leyte Samar Student Organization (LEYSAM)", "Regional student organization.", "Non-Academic",
                                "imgs/leysam.jpg", Arrays.asList("President: Paolo Mercado")),
                        create("Sinag ng Hilaga", "Cultural organization.", "Non-Academic",
                                "imgs/sanag.jpg", Arrays.asList("President: Nina Castillo")),
                        create("Kappa Nu Gamma", "Academic sorority.", "Academic",
                                "imgs/kappa.jpg", Arrays.asList("President: Sophia dela Rosa")),
                        create("Physical Education Student Association (PESA)", "PE student organization.", "Academic",
                                "imgs/pesa.jpg", Arrays.asList("President: Marco Aquino"))
                ));
            }
        };
    }

    private Organization create(String name, String desc, String category, String logo, List<String> officers) {
        Organization o = new Organization();
        o.setName(name);
        o.setDescription(desc);
        o.setCategory(category);
        o.setLogoUrl(logo);
        o.setOfficers(officers);
        return o;
    }
}