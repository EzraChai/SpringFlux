package com.stpd.springboot.votingsystem.Controller;

import com.stpd.springboot.votingsystem.Repository.CandidatesRepo;
import com.stpd.springboot.votingsystem.Repository.VotersRepo;
import com.stpd.springboot.votingsystem.model.Candidates;
import com.stpd.springboot.votingsystem.model.Voters;
import com.stpd.springboot.votingsystem.utils.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class VoterController {

    @Autowired
    private VotersRepo votersRepo;

    @Autowired
    private CandidatesRepo candidatesRepo;

    @GetMapping("/")
    public String goToVote(){
        return "index";
    }

    @GetMapping("/add/vote")
    public String addVote(){
        return "addvote";
    }

    @GetMapping("delete/candidates")
    public String deleteCandidates(){
        candidatesRepo.deleteAll();
        return "CandidatesDeleted";
    }

    @PostMapping("/add/voter")
    public String addVoter(@RequestParam String name){
        Voters voter = new Voters();
        voter.setName(name);
        voter.setHasVoted(false);
        Voters save = votersRepo.save(voter);
        return "addedVoter";
    }

    @PostMapping("/add/candidate")
    public String addcandidate(@RequestParam String name){
        Candidates candidate = new Candidates();
        candidate.setName(name);
        candidate.setNumberOfVotes(0);
        candidatesRepo.save(candidate);
        return  "addedCandidate";
    }

    @GetMapping("/add/candidate")
    public String addcandidate(){
        return "addcandidate";
    }

    @PostMapping("/dologin")
    public String dologin(@RequestParam String name, Model model, HttpSession session){
        List<Voters> usersWithPartOfName = votersRepo.findUsersWithPartOfName(name);
        if (usersWithPartOfName.size() == 0){
            return "nameNotFound";
        }
        if (usersWithPartOfName.size() > 1){
            return "opps";
        }else{
            Voters voters = usersWithPartOfName.get(0);
            if(voters.isHasVoted()){
                return "hadVote";
            }
            session.setAttribute("voter",voters);

            List<Candidates> all = candidatesRepo.findAll();
            model.addAttribute("Candidates",all);
            model.addAttribute("Voter",voters);

            return "performVote";
        }
    }

    @GetMapping("/get/candidates")
    public String getCandidates(Model model){
        Map<String, Integer> graphData = new TreeMap<>();
        List<Candidates> all = candidatesRepo.findAllByOrderByNumberOfVotesDesc();

        for (Candidates candidate : all){
            graphData.put(candidate.getName(), candidate.getNumberOfVotes());
        }
        model.addAttribute("CandidatesForChart",graphData);
        model.addAttribute("Candidates",all);
        return "candidates";
    }

    @GetMapping("/reset/all/vote")
    public String resetAllVote(){

        List<Candidates> all1 = candidatesRepo.findAll();
        for (Candidates candidate : all1){
            candidate.setNumberOfVotes(0);
            candidatesRepo.save(candidate);
        }

        List<Voters> all = votersRepo.findAll();
        for (Voters voter :all){
            voter.setHasVoted(false);
            votersRepo.save(voter);
        }
        return "reset";
    }

    @GetMapping("/voteFor")
    public String voteFor(@RequestParam int id, HttpSession session){
        Voters v = (Voters) session.getAttribute("voter");

        if (v.isHasVoted()){
            return "hadVote";
        }

        Optional<Candidates> byId = candidatesRepo.findById((long) id);
        Candidates candidate = byId.get();
        candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
        candidatesRepo.save(candidate);

        v.setHasVoted(true);
        votersRepo.save(v);

        return "voted";
    }

    @GetMapping("/group/{number}")
    public String groupMember(@PathVariable("number") int number,Model model){

        List<Voters> all = votersRepo.findAll();
        Map map = GroupMember.randomGroupList(all,number);
        model.addAttribute("GroupsMap",map);
        return "groupMember";
    }


}
