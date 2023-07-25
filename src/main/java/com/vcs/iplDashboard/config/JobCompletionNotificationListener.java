package com.vcs.iplDashboard.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcs.iplDashboard.model.Team;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

//  private final JdbcTemplate jdbcTemplate;

	private final EntityManager em;

	@Autowired
//  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
	public JobCompletionNotificationListener(EntityManager em) {
//	  this.jdbcTemplate = jdbcTemplate;
		this.em = em;
	}

	@Override
	@Transactional
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

//      System.out.println();
//      jdbcTemplate.query("SELECT date, team1, team2 FROM match",
//        (rs, row) -> {
//        	
//        	String t = String.format("%-30s", rs.getString(2));
//        	String s = "Date " + rs.getString(1) + "    Team 1 " + t + " Team 2 " + rs.getString(2);
//        	return s;
//        })
//      	.forEach(str -> System.out.println(str));

			Map<String, Team> teamData = new HashMap<>();

			em.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
					.getResultList()
					.stream()
					.map(e -> new Team((String) e[0], (long) e[1]))
					.forEach(team -> teamData.put(team.getTeamName(), team));
			
			em.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
					.getResultList()
					.stream()
					.forEach(e -> {
						Team team = teamData.get((String) e[0]);
						team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
					});
			
			em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
					.getResultList()
					.stream()
					.forEach(e -> {
						Team team = teamData.get((String) e[0]);
						if (team!=null) 
							team.setTotalWins((long) e[1]);
					});

			teamData.values().forEach(team -> em.persist(team));
//			teamData.values().forEach(team -> System.out.println(team));
		}	
	}
}
