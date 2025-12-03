package com.cms.assignment.complaint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByConsumerId(Integer consumerId);

    @Query("SELECT c FROM Complaint c WHERE c.assignedAgent.id = :agentId")
    List<Complaint> findByAssignedAgentId(Integer agentId);
    @Query("SELECT c FROM Complaint c WHERE c.assignedAgent.id = :agentId AND c.status != 'SOLVED'")
    List<Complaint> findActiveByAgentId(Integer agentId);

    @Query(value = "SELECT * FROM complaint WHERE agent_id IS NULL AND status = 'PENDING' ORDER BY submission_date ASC LIMIT 1", nativeQuery = true)
    Optional<Complaint> findFirstUnassigned();

}
