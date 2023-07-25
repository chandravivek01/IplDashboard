package com.vcs.iplDashboard.model;

import javax.persistence.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Entity
@Data
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String teamName;
	private long totalMatches;
	private long totalWins;

	@Transient
	private List<Match> matches;
	
	public Team(String teamName, long totalMatches) {
		this.teamName = teamName;
		this.totalMatches = totalMatches;
	}
}
