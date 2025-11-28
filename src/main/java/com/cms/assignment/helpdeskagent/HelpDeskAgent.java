package com.cms.assignment.helpdeskagent;

import com.cms.assignment.appuser.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class HelpDeskAgent extends User {

    private String employeeId;

    private String department;

}
