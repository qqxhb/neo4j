package com.qqxhb.neo4j.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qqxhb.neo4j.entity.SGNode;
import com.qqxhb.neo4j.repository.SGNodeReponsitory;


@Service
public class Neo4jService {

    @Autowired
    SGNodeReponsitory sgNodeReponsitory;

	public void deleteAll() {
		sgNodeReponsitory.deleteAll();
		
	}

	public void save(SGNode entity) {
		sgNodeReponsitory.save(entity);
	}

	public SGNode findByName(String name) {
		return sgNodeReponsitory.findByName(name);
	}

	public void saveAll(List<SGNode> entities) {
		sgNodeReponsitory.saveAll(entities);
	}

	public Iterable<SGNode> findAllById(List<Long> ids, int depth) {
		return sgNodeReponsitory.findAllById(ids, depth);
	}

}
