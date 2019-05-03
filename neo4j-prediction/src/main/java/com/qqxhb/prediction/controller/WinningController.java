package com.qqxhb.prediction.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.qqxhb.prediction.domain.Team;
import com.qqxhb.prediction.model.TeamQo;
import com.qqxhb.prediction.repository.PlayoffRepository;
import com.qqxhb.prediction.repository.RatioRepository;
import com.qqxhb.prediction.repository.WinningRepository;

@Controller
@RequestMapping("/winning")
public class WinningController {
    private static Logger logger = LoggerFactory.getLogger(WinningController.class);

    @Autowired
    private RatioRepository ratioRepository;
    @Autowired
    private PlayoffRepository playoffRepository;
    @Autowired
    private WinningRepository winningRepository;

    @RequestMapping("/index")
    public String index(ModelMap model) throws Exception{
        Iterable<Team> teams = ratioRepository.findAll();
        model.addAttribute("teams",teams);
        return "winning/index";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Map<String,Object>> getList(TeamQo teamQo) {
        try {
            Pageable pageable = PageRequest.of(teamQo.getPage(), teamQo.getSize(), null);
            Set<Map<String,Object>> maps = ratioRepository.findHistoryByTeamName(teamQo.getName(), teamQo.getPage() * teamQo.getSize(), teamQo.getSize());
            int count = ratioRepository.findHistoryByTeamNameCount(teamQo.getName());
            return new PageImpl<Map<String,Object>>(new ArrayList<Map<String,Object>>(maps), pageable, (long)count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model){
        Iterable<Team> teams = ratioRepository.findAll();
        Iterable<Playoff> playoffs = playoffRepository.findPlayoff();
        model.addAttribute("teams", teams);
        model.addAttribute("playoffs", playoffs);
        return "winning/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request) throws Exception{
        String t1 = request.getParameter("t1");
        String t2 = request.getParameter("t2");
        String win = request.getParameter("win");
        String loss = request.getParameter("loss");
        String pid = request.getParameter("pid");

        Playoff playoff = playoffRepository.findById(new Long(pid)).get();

        Team team1 = ratioRepository.findById(new Long(t1)).get();
        team1.win(new Integer(win), playoff);
        ratioRepository.save(team1);

        Team team2 =  ratioRepository.findById(new Long(t2)).get();
        team2.win(new Integer(loss), playoff);
        ratioRepository.save(team2);

        logger.info("ÐÂÔö->ID="+playoff.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{wid}/{lid}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long wid, @PathVariable Long lid) throws Exception{
        if(wid != null) {
            winningRepository.deleteById(wid);
            logger.info("É¾³ý->WID=" + wid);
        }
        if(lid != null){
            winningRepository.deleteById(lid);
            logger.info("É¾³ý->LID=" + lid);
        }
        return "1";
    }

}
