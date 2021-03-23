package com.example.fireproject01;

public class Patient {
    String patientName;
    String patientPassword;

    public Patient(String patientName, String patientPassword) {
        this.patientName = patientName;
        this.patientPassword = patientPassword;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientPassword() {
        return patientPassword;
    }
}
