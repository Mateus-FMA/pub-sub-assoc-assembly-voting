package com.mateusfma.assemblyvoting.controller.rest.request;

import javax.validation.constraints.NotEmpty;

public class CountVoteRequest {

    @NotEmpty
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
