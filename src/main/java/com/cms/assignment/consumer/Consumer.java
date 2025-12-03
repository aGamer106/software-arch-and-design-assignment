package com.cms.assignment.consumer;

import com.cms.assignment.appuser.User;
import com.cms.assignment.complaint.Complaint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Consumer extends User {

    private String phoneNumber;

    @OneToMany(mappedBy = "consumer")
    @JsonIgnore
    private List<Complaint> complaints;

}
