package com.mateusfma.assemblyvoting.controller.rest.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VoteRequest {

    @NotNull
    private Long associateId;

    @NotEmpty
    private String topicName;

    @NotEmpty
    private String vote;

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
