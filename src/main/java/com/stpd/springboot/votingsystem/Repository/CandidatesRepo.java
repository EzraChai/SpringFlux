package com.stpd.springboot.votingsystem.Repository;

import com.stpd.springboot.votingsystem.model.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatesRepo extends JpaRepository<Candidates, Long> {
}
