package com.cms.assignment.complaint;

import com.cms.assignment.ComplaintStatus;
import com.cms.assignment.appuser.UserRepository;
import com.cms.assignment.consumer.Consumer;
import com.cms.assignment.dto.AgentStatsDTO;
import com.cms.assignment.helpdeskagent.HelpDeskAgent;
import com.cms.assignment.complaint.ComplaintRepository;
import com.cms.assignment.service.EmailService;
import com.mailjet.client.resource.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/consumer/complaints/{consumerId}")
    public List<Complaint> getMyComplaints(@PathVariable Integer consumerId) {
        return complaintRepository.findByConsumerId(consumerId);
    }

    @PostMapping("/consumer/complaints/{consumerId}")
    public Complaint createComplaint(@PathVariable Integer consumerId, @RequestBody Complaint complaint) {
        Consumer consumer = (Consumer) userRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        complaint.setConsumer(consumer);
        complaint.setSubmissionDate(LocalDate.now());
        complaint.setStatus(ComplaintStatus.PENDING);

        return complaintRepository.save(complaint);
    }

    @GetMapping("/agent/complaints/all")
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @GetMapping("/agent/complaints/{agentId}")
    public List<Complaint> getAgentComplaints(@PathVariable Integer agentId) {
        return complaintRepository.findByAssignedAgentId(agentId);
    }

    @PutMapping("/agent/complaints/{complaintId}/solve")
    public Complaint markAsSolved(@PathVariable Integer complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(ComplaintStatus.SOLVED);
        return complaintRepository.save(complaint);
    }

    @PostMapping("/agent/assign/{agentId}")
    public Complaint assignNextTicket(@PathVariable Integer agentId) {
        HelpDeskAgent agent = (HelpDeskAgent) userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        List<Complaint> activeTickets = complaintRepository.findActiveByAgentId(agentId);
        if (!activeTickets.isEmpty()) {
            throw new RuntimeException("Agent is busy. Please resolve current tickets first.");
        }

        Optional<Complaint> nextTicket = complaintRepository.findFirstUnassigned();

        if (nextTicket.isPresent()) {
            Complaint c = nextTicket.get();
            c.setAssignedAgent(agent);
            c.setStatus(ComplaintStatus.REVIEWED);
            return complaintRepository.save(c);
        } else {
            throw new RuntimeException("No new tickets available.");
        }
    }

    @PostMapping("/agent/complaints/{complaintId}/escalate")
    public Complaint escalateToSupport(@PathVariable Integer complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        String supportEmail = "razvandanielbesleaga@gmail.com";

        emailService.sendEscalationEmail(
                supportEmail,
                complaint.getTitle(),
                complaint.getConsumer().getName(),
                complaint.getConsumer().getPhoneNumber(),
                complaint.getDescription()
        );

        return complaintRepository.save(complaint);
    }

    @GetMapping("/manager/analytics")
    public List<AgentStatsDTO> getAgentAnalytics() {
        return complaintRepository.getAgentPerformanceStats();
    }


}