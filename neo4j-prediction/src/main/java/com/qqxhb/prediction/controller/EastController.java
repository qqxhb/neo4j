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

import com.qqxhb.prediction.domain.East;
import com.qqxhb.prediction.model.TeamQo;
import com.qqxhb.prediction.repository.EastTeamRepository;

@Controller
@RequestMapping("/east")
public class EastController {
	private static Logger logger = LoggerFactory.getLogger(EastController.class);

	@Autowired
	EastTeamRepository eastTeamRepository;
	
	@RequestMapping("/index")
	public String index(ModelMap model) throws Exception {
		return "east/index";
	}

	@RequestMapping(value = "/{id}")
	public String show(ModelMap model, @PathVariable Long id) {
		East east = eastTeamRepository.findOne(id);
		model.addAttribute("team", east);
		return "east/show";
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public Page<East> getList(TeamQo teamQo) {
		try {
			Pageable pageable = PageRequest.of(teamQo.getPage(), teamQo.getSize(), null);
			Set<East> easts = eastTeamRepository.findEast(teamQo.getName(), teamQo.getPage() * teamQo.getSize(),
					teamQo.getSize());
			int count = eastTeamRepository.findEastCount(teamQo.getName());
			return new PageImpl<East>(new ArrayList<East>(easts), pageable, (long) count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/new")
	public String create(ModelMap model, East east) {
		model.addAttribute("team", east);
		return "east/new";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(East east) throws Exception {
		eastTeamRepository.save(east);
		logger.info("����->ID=" + east.getId());
		return "1";
	}

	@RequestMapping(value = "/edit/{id}")
	public String update(ModelMap model, @PathVariable Long id) {
		Optional<East> east = eastTeamRepository.findById(id);
		model.addAttribute("team", east.get());
		return "east/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	@ResponseBody
	public String update(East east) throws Exception {
		eastTeamRepository.save(east);
		logger.info("�޸�->ID=" + east.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		eastTeamRepository.deleteById(id);
		logger.info("ɾ��->ID=" + id);
		return "1";
	}

}
