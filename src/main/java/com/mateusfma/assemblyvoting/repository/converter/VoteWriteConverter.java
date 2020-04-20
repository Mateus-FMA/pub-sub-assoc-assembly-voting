package com.mateusfma.assemblyvoting.repository.converter;

import com.mateusfma.assemblyvoting.entity.Vote;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;

@WritingConverter
public class VoteWriteConverter implements Converter<Vote, OutboundRow> {

    @Override
    public OutboundRow convert(Vote source) {
        OutboundRow row = new OutboundRow();
        row.put("associate_id", SettableValue.from(source.getAssociateId()));
        row.put("topic_id", SettableValue.from(source.getTopicId()));
        row.put("vote_value", SettableValue.from(source.getValue()));

        return row;
    }
}
