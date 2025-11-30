package com.cms.assignment.complaint;

import com.cms.assignment.ComplaintStatus;
import com.cms.assignment.appuser.UserRepository;
import com.cms.assignment.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consumer/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{consumerId}")
    public List<Complaint> getMyComplaints(@PathVariable Integer consumerId) {
        return complaintRepository.findByConsumerId(consumerId);
    }

    @PostMapping("{consumerId}")
    public Complaint createComplaint(@PathVariable Integer consumerId, @RequestBody Complaint complaint) {
        Consumer consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(() -> new RuntimeException("Consumer not found"));
        complaint.setConsumer(consumer);
        complaint.setSubmissionDate(LocalDate.now());
        complaint.setStatus(ComplaintStatus.PENDING);
        return complaintRepository.save(complaint);
    }

}
