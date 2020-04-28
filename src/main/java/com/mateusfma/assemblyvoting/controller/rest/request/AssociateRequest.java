package com.mateusfma.assemblyvoting.controller.rest.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AssociateRequest {

    @NotNull
    @Pattern(regexp = "[\\d]{11}")
    private String cpf;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotNull
    private Integer age;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
