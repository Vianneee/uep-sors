package com.uep.sors.service;

import com.uep.sors.entity.Organization;
import com.uep.sors.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    public List<Organization> getByCategory(String category) {
        return organizationRepository.findByCategoryIgnoreCase(category);
    }

    public List<Organization> searchByName(String name) {
        return organizationRepository.findByNameContainingIgnoreCase(name);
    }
}
