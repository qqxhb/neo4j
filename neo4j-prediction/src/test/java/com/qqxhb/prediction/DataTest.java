package com.qqxhb.prediction;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.qqxhb.prediction.domain.East;
import com.qqxhb.prediction.domain.Playoff;
import com.qqxhb.prediction.domain.West;
import com.qqxhb.prediction.repository.EastTeamRepository;
import com.qqxhb.prediction.repository.RatioRepository;
import com.qqxhb.prediction.repository.WestTeamRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataTest {
	private static Logger log = LoggerFactory.getLogger(DataTest.class);

	@Autowired
	private WestTeamRepository westTeamRepository;
	@Autowired
	private EastTeamRepository eastTeamRepository;
	@Autowired
	private RatioRepository ratioRepository;

	@Before
	public void add() {
		Playoff playoff = new Playoff();
		playoff.setYear("2016");
		playoff.setRound("第一回合");

		East east = new East();
		east.setName("东部一队");
		east.setCode("东一");
		east.win(2, playoff);
		eastTeamRepository.save(east);
		Assert.notNull(east.getId(), "ID为空");

		West west = new West();
		west.setName("西部一队");
		west.setCode("西一");
		west.win(4, playoff);
		westTeamRepository.save(west);
		Assert.notNull(west.getId(), "ID为空");
	}

	@Test
	public void get() {
		Set<Map<String, Object>> maps = ratioRepository.findWinAndLoss("东部一队", "西部一队");
		Assert.notEmpty(maps, "结果为空");
		log.info("\n==========Year:{} =============", maps.iterator().next().get("year"));
	}
}
