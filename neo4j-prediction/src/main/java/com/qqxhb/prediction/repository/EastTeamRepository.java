package com.qqxhb.prediction.repository;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qqxhb.prediction.domain.East;

@Repository
public interface EastTeamRepository extends Neo4jRepository<East, Long> {
	@Query("MATCH (t:E) WHERE t.name =~ ('(?i).*'+{name}+'.*') " + "RETURN t ORDER BY t.name SKIP {skip} LIMIT {limit}")
	Set<East> findEast(@Param("name") String name, @Param("skip") Integer skip, @Param("limit") Integer limit);

	@Query("MATCH (t:E) WHERE t.name =~ ('(?i).*'+{name}+'.*') " + "RETURN COUNT(t) as count")
	Integer findEastCount(@Param("name") String name);

	@Query("MATCH (t:E) WHERE t.id ={id} RETURN t ")
	East findOne(@Param("id") Long id);
}
