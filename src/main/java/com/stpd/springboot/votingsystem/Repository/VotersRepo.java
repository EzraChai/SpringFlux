package com.stpd.springboot.votingsystem.Repository;

import com.stpd.springboot.votingsystem.model.Voters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotersRepo extends JpaRepository<Voters, Long> {

    @Query("SELECT u FROM Voters u WHERE u.name LIKE CONCAT('%',:name,'%')")
    List<Voters> findUsersWithPartOfName(@Param("name") String name);
}
