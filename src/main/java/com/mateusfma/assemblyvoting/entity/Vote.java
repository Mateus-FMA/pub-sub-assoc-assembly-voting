package com.mateusfma.assemblyvoting.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("vote")
public class Vote {

    private Long associateId;
    private Long topicId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return associateId.equals(vote.associateId) &&
                topicId.equals(vote.topicId) &&
                value.equals(vote.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associateId, topicId, value);
    }

    @Column("vote_value")
    private Boolean value;

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
