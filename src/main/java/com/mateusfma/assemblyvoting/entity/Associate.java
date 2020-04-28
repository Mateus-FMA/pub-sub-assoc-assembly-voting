package com.mateusfma.assemblyvoting.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("associate")
public class Associate {

    @Id
    @Column("associate_id")
    private Long id;

    private String cpf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Associate associate = (Associate) o;
        return id.equals(associate.id) &&
                cpf.equals(associate.cpf) &&
                name.equals(associate.name) &&
                age.equals(associate.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, name, age);
    }

    @Column("associate_name")
    private String name;

    private Integer age;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
