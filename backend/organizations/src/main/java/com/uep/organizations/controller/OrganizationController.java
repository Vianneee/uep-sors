package com.uep.organizations.controller;

import com.uep.organizations.model.Organization;
import com.uep.organizations.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public List<Organization> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        if (category != null) {
            return organizationService.getByCategory(category);
        }
        if (search != null) {
            return organizationService.searchByName(search);
        }
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public Optional<Organization> getById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }
}