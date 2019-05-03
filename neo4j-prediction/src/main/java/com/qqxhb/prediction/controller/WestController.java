package com.qqxhb.prediction.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qqxhb.prediction.domain.West;
import com.qqxhb.prediction.model.TeamQo;
import com.qqxhb.prediction.repository.WestTeamRepository;

@Controller
@RequestMapping("/west")
public class WestController {
	private static Logger logger = LoggerFactory.getLogger(WestController.class);

	@Autowired
	WestTeamRepository westTeamRepository;

	@RequestMapping("/index")
	public String index(ModelMap model) throws Exception {
		return "west/index";
	}

	@RequestMapping(value = "/{id}")
	public String show(ModelMap model, @PathVariable Long id) {
		Optional<West> west = westTeamRepository.findById(id);
		model.addAttribute("team", west.get());
		return "west/show";
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public Page<West> getList(TeamQo teamQo) {
		try {
			Pageable pageable = PageRequest.of(teamQo.getPage(), teamQo.getSize(), null);
			Set<West> wests = westTeamRepository.findWest(teamQo.getName(), teamQo.getPage() * teamQo.getSize(),
					teamQo.getSize());
			int count = westTeamRepository.findWestCount(teamQo.getName());
			return new PageImpl<West>(new ArrayList<West>(wests), pageable, (long) count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/new")
	public String create(ModelMap model, West west) {
		model.addAttribute("team", west);
		return "west/new";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(West west) throws Exception {
		westTeamRepository.save(west);
		logger.info("ÐÂÔö->ID=" + west.getId());
		return "1";
	}

	@RequestMapping(value = "/edit/{id}")
	public String update(ModelMap model, @PathVariable Long id) {
		Optional<West> west = westTeamRepository.findById(id);
		model.addAttribute("team", west.get());
		return "west/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	@ResponseBody
	public String update(West west) throws Exception {
		westTeamRepository.save(west);
		logger.info("ÐÞ¸Ä->ID=" + west.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		westTeamRepository.deleteById(id);
		logger.info("É¾³ý->ID=" + id);
		return "1";
	}

}
