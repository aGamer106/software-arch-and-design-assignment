package com.cms.assignment.consumer;

import com.cms.assignment.complaint.Complaint;

import java.time.LocalDate;

public class Consumer {

    private int id;

    private String name;

    private String email;

    private LocalDate dateOfBirth;

    private enum preferredContactMethod {
        PHONE,
        EMAIL
    }

    public Complaint submitComplaint(String details) {
        return null;
    }

    public String getComplaintStatus(int complaintId) {
        return null;
    }

    public boolean provideResolutionFeedback(int complaintId, String status) {
        return true;
    }



}
