package com.stpd.springboot.votingsystem.utils;


import com.stpd.springboot.votingsystem.Repository.VotersRepo;
import com.stpd.springboot.votingsystem.model.Voters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class GroupMember {

    @Autowired
    private VotersRepo votersRepo;


    public static Map<Integer, List<Voters>> randomGroupList(List<Voters> list, int number) {
        Map<Integer, List<Voters>> map = new HashMap<Integer, List<Voters>>();
        int listSize = list.size();
        int groupMemberInAGroup = listSize / number;

        if(number == 0){
            return map;
        }

        if(number > listSize){
            number = listSize;
        }

        if(number <= 0){
            return map;
        }


        Collections.shuffle(list, new Random(3));

        //listSize = 34; number = 4; groupMember = 8;
        System.out.println("groupMemberInAGroup = " + groupMemberInAGroup);


        for (int i = 0; i < number; i++) {
            List<Voters> randomList = new ArrayList<Voters>();
            for (int j = 0; j < groupMemberInAGroup; j++){
                Random random = new Random();
                int randomNumber = random.nextInt(listSize);
                Voters voters = list.get(randomNumber);
                randomList.add(voters);
                list.remove(randomNumber);
                listSize = list.size();
            }
            map.put(i,randomList);
        }

        if(listSize != 0){
            while(list.size() != 0){
                int currentIndex = list.size() - 1;
                Voters voter = list.get(currentIndex);
                List<Voters> voters = map.get(number - list.size());
                voters.add(voter);
                list.remove(currentIndex);
            }
        }
        return map;
    }
}
