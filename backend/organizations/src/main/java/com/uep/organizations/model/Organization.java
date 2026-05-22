package com.uep.organizations.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;
    private String logoUrl;

    @ElementCollection
    @CollectionTable(name = "organization_officers", joinColumns = @JoinColumn(name = "organization_id"))
    @Column(name = "officer")
    private List<String> officers;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public List<String> getOfficers() { return officers; }
    public void setOfficers(List<String> officers) { this.officers = officers; }
}