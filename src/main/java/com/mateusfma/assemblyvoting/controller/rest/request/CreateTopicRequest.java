package com.mateusfma.assemblyvoting.controller.rest.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateTopicRequest {

    @NotEmpty
    @Size(max = 30)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
