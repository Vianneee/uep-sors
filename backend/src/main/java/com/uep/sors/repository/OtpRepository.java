package com.uep.sors.repository;

import com.uep.sors.entity.OtpCode;
import com.uep.sors.entity.OtpCode.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpCode, Long> {

    @Query("SELECT o FROM OtpCode o WHERE o.email = :email AND o.type = :type AND o.used = false AND o.expiresAt > CURRENT_TIMESTAMP ORDER BY o.createdAt DESC")
    Optional<OtpCode> findLatestValid(String email, OtpType type);

    @Modifying
    @Transactional
    @Query("UPDATE OtpCode o SET o.used = true WHERE o.email = :email AND o.type = :type AND o.used = false")
    void invalidateAll(String email, OtpType type);
}