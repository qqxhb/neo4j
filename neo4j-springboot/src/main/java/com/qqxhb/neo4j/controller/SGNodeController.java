package com.qqxhb.neo4j.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qqxhb.neo4j.entity.SGNode;
import com.qqxhb.neo4j.service.Neo4jService;

@RestController
@RequestMapping("/sg")
public class SGNodeController {

    @Autowired
    Neo4jService neo4jService;

    @DeleteMapping("/delete")
    public String delete() {
    	neo4jService.deleteAll();
        return "OK";
    }

    @GetMapping("/add")
    public String add() {
        addNodes();
        return "OK";
    }

    @GetMapping("/get")
    public Iterable<SGNode> relation() {
        SGNode node = neo4jService.findByName("tsp");
        List<Long> ids = new ArrayList<>();
        ids.add(node.getId());
        Iterable<SGNode> result = neo4jService.findAllById(ids, 1);
        return result;
    }


    private void addNodes() {
        neo4jService.deleteAll();

        List<SGNode> list = new ArrayList<>();

        SGNode node = new SGNode("tsp");
        list.add(node);

        for (Integer i = 1; i <= 10; i++) {
            node = new SGNode("tsp" + i);
            node.setCount(new Random().nextLong());
            node.setError(new Random().nextLong());
            node.setMax(new Random().nextDouble());
            node.setMin(new Random().nextDouble());
            list.add(node);
        }

        neo4jService.saveAll(list);

        SGNode start = neo4jService.findByName("tsp1");
        SGNode end = neo4jService.findByName("tsp");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp2");
        end = neo4jService.findByName("tsp");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp9");
        end = neo4jService.findByName("tsp7");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp7");
        end = neo4jService.findByName("tsp2");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp2");
        end = neo4jService.findByName("tsp8");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp");
        end = neo4jService.findByName("tsp3");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp");
        end = neo4jService.findByName("tsp4");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp6");
        end = neo4jService.findByName("tsp3");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp3");
        end = neo4jService.findByName("tsp5");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);

        start = neo4jService.findByName("tsp5");
        end = neo4jService.findByName("tsp10");
        start.addCalls(end, new Random().nextLong());
        neo4jService.save(start);
    }
}