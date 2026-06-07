package com.uep.sors.repository;

import com.uep.sors.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByCategoryIgnoreCase(String category);
    List<Organization> findByNameContainingIgnoreCase(String name);
}
