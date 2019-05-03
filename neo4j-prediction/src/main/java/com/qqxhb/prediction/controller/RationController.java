package com.qqxhb.prediction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qqxhb.prediction.domain.Team;
import com.qqxhb.prediction.model.TeamQo;
import com.qqxhb.prediction.repository.EastTeamRepository;
import com.qqxhb.prediction.repository.RatioRepository;

@Controller
@RequestMapping("/ration")
public class RationController {

    @Autowired
    private RatioRepository ratioRepository;
    @Autowired
    EastTeamRepository eastTeamRepository;

    @RequestMapping("/index")
    public String index(ModelMap model) throws Exception{
        return "ration/index";
    }

    @RequestMapping(value = "/ration_list")
    @ResponseBody
    public Page<Map<String,Object>> getList(TeamQo teamQo) {
        try {
            Pageable pageable =  PageRequest.of(teamQo.getPage(), teamQo.getSize(), null);
            Set<Map<String,Object>> maps = ratioRepository.findPercentage(teamQo.getPage() * teamQo.getSize(), teamQo.getSize());
            int count = ratioRepository.findPercentageCount();
            return new PageImpl<Map<String,Object>>(new ArrayList<Map<String,Object>>(maps), pageable, (long)count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/winloss")
    public String win_loss(ModelMap model) throws Exception{
        Iterable<Team> teams = ratioRepository.findAll();
        model.addAttribute("teams", teams);
        return "ration/win_loss";
    }

    @RequestMapping(value = "/win_loss")
    @ResponseBody
    public Map<String,Object> getWin_Loss(HttpServletRequest request) {
        try {
            String t1 = request.getParameter("t1");
            String t2 = request.getParameter("t2");
            Map<String,Object> data = new HashMap<>();
            Set<Map<String,Object>> maps = ratioRepository.findWinAndLoss(t1, t2);
            if(maps != null && maps.size() > 0){
                data.put("met", 1);
                data.put("content", maps);
            }else{
                maps = ratioRepository.findNeverMetPaths(t1, t2);
                data.put("met", 2);
                data.put("content", maps);
            }
            float netwin = ratioRepository.findAvgNetWin(t1, t2);
            data.put("netwin", netwin);
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
