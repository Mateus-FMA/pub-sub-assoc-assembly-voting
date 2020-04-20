package com.mateusfma.assemblyvoting.repository;

import com.mateusfma.assemblyvoting.entity.Vote;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class VoteRepositoryImpl implements VoteRepository {

    private DatabaseClient client;

    public VoteRepositoryImpl(@Autowired ConnectionFactory factory) {
        client = DatabaseClient.create(factory);
    }

    @Override
    public Mono<Vote> save(Vote vote) {
        String sql = "SELECT * FROM vote WHERE associate_id = $1 AND topic_id = $2";

        return client.insert()
                .into(Vote.class)
                .using(vote)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> client.execute(sql)
                    .bind(0, vote.getAssociateId())
                    .bind(1, vote.getTopicId())
                    .as(Vote.class)
                    .fetch()
                    .one());
    }

    @Override
    public Mono<Long> countVotes(Long id, Boolean value) {
        String query = "SELECT count(*) FROM vote WHERE topic_id = $1 AND vote_value = $2";
        return client.execute(query)
                .bind(0, id)
                .bind(1, value)
                .as(Long.class)
                .fetch()
                .one();
    }
}
