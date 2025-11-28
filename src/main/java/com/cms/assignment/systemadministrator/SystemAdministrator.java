package com.cms.assignment.systemadministrator;

import com.cms.assignment.appuser.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemAdministrator extends User {

    private String adminCode;

    private LocalDateTime lastSystemAudit;

}
