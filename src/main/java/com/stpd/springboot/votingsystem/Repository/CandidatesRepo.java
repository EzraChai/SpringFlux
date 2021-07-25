package com.stpd.springboot.votingsystem.Repository;

import com.stpd.springboot.votingsystem.model.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatesRepo extends JpaRepository<Candidates, Long> {
    List<Candidates> findAllByOrderByNumberOfVotesDesc();
}
