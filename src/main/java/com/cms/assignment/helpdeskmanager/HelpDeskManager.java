package com.cms.assignment.helpdeskmanager;

import com.cms.assignment.appuser.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class HelpDeskManager extends User {

    private String regionManaged;

    private String managementLevel;

}
