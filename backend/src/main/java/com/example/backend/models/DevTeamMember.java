package com.example.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "User")
public class DevTeamMember extends User {
    private String specialization;
    private List<String> skills;

    public DevTeamMember() {
        super();
        setRole(UserRole.DEV_TEAM_MEMBER);
    }

    public DevTeamMember(String name, String password, String email, String phoneNumber, 
                         String profilePicture, String specialization, List<String> skills) {
        super(name, password, email, phoneNumber, profilePicture, UserRole.DEV_TEAM_MEMBER, null);
        this.specialization = specialization;
        this.skills = skills;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    @Override
    public String toString() {
        return "DevTeamMember{specialization='" + specialization + "', skills=" + skills + "} " + super.toString();
    }
}