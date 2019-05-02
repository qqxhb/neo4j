package com.qqxhb.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qqxhb.neo4j.entity.SGNode;

@Repository
public interface SGNodeReponsitory extends Neo4jRepository<SGNode, Long> {
	// 此处用法可见
	// https://docs.spring.io/spring-data/neo4j/docs/5.1.2.RELEASE/reference/html/#_query_methods
	SGNode findByName(@Param("name") String name);
}