package com.mateusfma.assemblyvoting.repository.converter;

import com.mateusfma.assemblyvoting.entity.Vote;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class VoteReadConverter implements Converter<Row, Vote> {

    @Override
    public Vote convert(Row source) {
        Vote vote = new Vote();
        vote.setAssociateId(source.get("associate_id", Long.class));
        vote.setTopicId(source.get("topic_id", Long.class));
        vote.setValue(source.get("vote_value", Boolean.class));

        return vote;
    }
}
