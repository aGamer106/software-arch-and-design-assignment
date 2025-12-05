package com.cms.assignment.complaint;

import com.cms.assignment.dto.AgentStatsDTO;
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

    @Query("SELECT new com.cms.assignment.dto.AgentStatsDTO(" +
            "a.name, a.department, " +
            "SUM(CASE WHEN c.status = 'SOLVED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN c.status != 'SOLVED' THEN 1 ELSE 0 END)) " +
            "FROM HelpDeskAgent a " +
            "LEFT JOIN Complaint c ON c.assignedAgent.id = a.id " +
            "GROUP BY a.id, a.name, a.department")
    List<AgentStatsDTO> getAgentPerformanceStats();

}
