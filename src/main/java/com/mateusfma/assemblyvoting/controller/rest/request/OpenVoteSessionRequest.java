package com.mateusfma.assemblyvoting.controller.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OpenVoteSessionRequest {

    @NotNull
    @Size(max = 30)
    private String topicName;
    private Integer durationSec;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }
}
