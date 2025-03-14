package com.example.backend.models;

import java.util.List;

public class ProjectManager extends User {
    private List<String> managedProjects;

    public ProjectManager() {
        super();
        setRole(UserRole.PROJECT_MANAGER);
    }

    public ProjectManager(String email, String password, List<String> managedProjects) {
        super(null, password, email, UserRole.PROJECT_MANAGER);
        this.managedProjects = managedProjects;
    }

    public List<String> getManagedProjects() { return managedProjects; }
    public void setManagedProjects(List<String> managedProjects) { this.managedProjects = managedProjects; }

    @Override
    public String toString() {
        return "ProjectManager{id='" + getId() + "', email='" + getEmail() + "', role=" + getRole() +
               ", managedProjects=" + managedProjects + "}";
    }
}