package com.vcs.iplDashboard.controller;

import com.vcs.iplDashboard.model.Match;
import com.vcs.iplDashboard.model.Team;
import com.vcs.iplDashboard.repository.MatchRepository;
import com.vcs.iplDashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {

        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesByTeam(teamName, 4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year+1, 1, 1);
//        return matchRepository.getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(
//                teamName, startDate, endDate,
//                teamName, startDate, endDate
//        );
        return matchRepository.getMatchesByTeamBetweenDates(teamName, startDate, endDate);
    }
}
