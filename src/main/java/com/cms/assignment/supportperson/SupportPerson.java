package com.cms.assignment.supportperson;


import com.cms.assignment.appuser.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SupportPerson extends User {

    private String expertiseArea;

    private boolean isAvailable;

}
