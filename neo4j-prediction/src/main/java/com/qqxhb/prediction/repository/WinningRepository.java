package com.qqxhb.prediction.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.qqxhb.prediction.domain.Winning;

@Repository
public interface WinningRepository extends Neo4jRepository<Winning,Long> {
}
