package com.mateusfma.assemblyvoting.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Table("topic")
public class Topic {

    @Id
    @Column("topic_id")
    private Long id;

    @Column("topic_name")
    private String name;

    @Column("is_opened")
    private Boolean open;

    @Column("start_time")
    private OffsetDateTime start;

    @Column("duration_sec")
    private Integer durationSec;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public void setStart(OffsetDateTime start) {
        this.start = start;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id.equals(topic.id) &&
                name.equals(topic.name) &&
                open.equals(topic.open) &&
                Objects.equals(start, topic.start) &&
                Objects.equals(durationSec, topic.durationSec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, open, start, durationSec);
    }
}
