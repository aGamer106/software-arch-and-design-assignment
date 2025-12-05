package com.cms.assignment.config;

import com.cms.assignment.ComplaintStatus;
import com.cms.assignment.Role;
import com.cms.assignment.appuser.UserRepository;
import com.cms.assignment.complaint.Complaint;
import com.cms.assignment.complaint.ComplaintRepository;
import com.cms.assignment.consumer.Consumer;
import com.cms.assignment.helpdeskagent.HelpDeskAgent;
import com.cms.assignment.helpdeskmanager.HelpDeskManager;
import com.cms.assignment.supportperson.SupportPerson;
import com.cms.assignment.systemadministrator.SystemAdministrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            System.out.println("Seed: Database empty. Seeding initial data...");

            Consumer consumer = new Consumer();
            consumer.setName("Priya Sharma");
            consumer.setEmail("priya@example.com");
            consumer.setPassword(passwordEncoder.encode("password123"));
            consumer.setRole(Role.CONSUMER);
            consumer.setPhoneNumber("07700900000");

            userRepository.save(consumer);

            Complaint c1 = new Complaint();
            c1.setTitle("Double Charge on Credit Card");
            c1.setDescription("I was charged twice for my transaction at Tesco yesterday.");
            c1.setSubmissionDate(LocalDate.now().minusDays(2));
            c1.setStatus(ComplaintStatus.PENDING);
            c1.setConsumer(consumer);

            Complaint c2 = new Complaint();
            c2.setTitle("App Crashing on Login");
            c2.setDescription("The mobile app closes immediately when I try to open it.");
            c2.setSubmissionDate(LocalDate.now().minusDays(5));
            c2.setStatus(ComplaintStatus.SOLVED);
            c2.setConsumer(consumer);

            complaintRepository.saveAll(List.of(c1, c2));

            HelpDeskAgent hda = new HelpDeskAgent();
            hda.setName("David Chen");
            hda.setEmail("david.agent@cms.com");
            hda.setPassword(passwordEncoder.encode("agent123"));
            hda.setRole(Role.HELP_DESK_AGENT);
            hda.setEmployeeId("HDA-001");
            hda.setDepartment("Level 1 Support");
            userRepository.save(hda);

            HelpDeskManager hdm = new HelpDeskManager();
            hdm.setName("Eddie Manager");
            hdm.setEmail("eddie.manager@cms.com");
            hdm.setPassword(passwordEncoder.encode("manager123"));
            hdm.setRole(Role.HELP_DESK_MANAGER);
            userRepository.save(hdm);

            SupportPerson sp = new SupportPerson();
            sp.setName("Sarah Tech");
            sp.setEmail("sarah.tech@cms.com");
            sp.setPassword(passwordEncoder.encode("support123"));
            sp.setRole(Role.HELP_DESK_AGENT);
            userRepository.save(sp);

            SystemAdministrator sa = new SystemAdministrator();
            sa.setName("Amy Admin");
            sa.setEmail("amy.admin@cms.com");
            sa.setPassword(passwordEncoder.encode("admin123"));
            sa.setRole(Role.ADMIN);
            userRepository.save(sa);

            System.out.println("Seed: Data seeding completed!");
        }
    }
}