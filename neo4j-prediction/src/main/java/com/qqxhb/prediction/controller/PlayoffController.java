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

import com.qqxhb.prediction.domain.Playoff;
import com.qqxhb.prediction.model.PlayoffQo;
import com.qqxhb.prediction.repository.PlayoffRepository;

@Controller
@RequestMapping("/playoff")
public class PlayoffController {
	private static Logger logger = LoggerFactory.getLogger(PlayoffController.class);

	@Autowired
	private PlayoffRepository playoffRepository;

	@RequestMapping("/index")
	public String index(ModelMap model) throws Exception {
		return "playoff/index";
	}

	@RequestMapping(value = "/{id}")
	public String show(ModelMap model, @PathVariable Long id) {
		Optional<Playoff> playoff = playoffRepository.findById(id);
		model.addAttribute("playoff", playoff.get());
		return "playoff/show";
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public Page<Playoff> getList(PlayoffQo playoffQo) {
		try {
			Pageable pageable = PageRequest.of(playoffQo.getPage(), playoffQo.getSize(), null);
			Set<Playoff> playoffs = playoffRepository.findPlayoff(playoffQo.getYear(),
					playoffQo.getPage() * playoffQo.getSize(), playoffQo.getSize());
			int count = playoffRepository.findPlayoffCount(playoffQo.getYear());
			return new PageImpl<Playoff>(new ArrayList<Playoff>(playoffs), pageable, (long) count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/new")
	public String create(ModelMap model, Playoff playoff) {
		model.addAttribute("playoff", playoff);
		return "playoff/new";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(Playoff playoff) throws Exception {
		playoffRepository.save(playoff);
		logger.info("����->ID=" + playoff.getId());
		return "1";
	}

	@RequestMapping(value = "/edit/{id}")
	public String update(ModelMap model, @PathVariable Long id) {
		Optional<Playoff> playoff = playoffRepository.findById(id);
		model.addAttribute("playoff", playoff.get());
		return "playoff/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	@ResponseBody
	public String update(Playoff playoff) throws Exception {
		playoffRepository.save(playoff);
		logger.info("�޸�->ID=" + playoff.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		playoffRepository.deleteById(id);
		logger.info("ɾ��->ID=" + id);
		return "1";
	}

}
