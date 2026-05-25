package com.uep.sors.repository;

import com.uep.sors.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByOrganizationId(Long organizationId);
    Optional<Application> findByOrganizationIdAndStudentNumber(Long organizationId, String studentNumber);
    List<Application> findByStudentNumber(String studentNumber);
}
