package com.mateusfma.assemblyvoting.router.rest.response;

public class CountVoteResponse {

    private Long yes;
    private Long no;

    public Long getYes() {
        return yes;
    }

    public void setYes(Long yes) {
        this.yes = yes;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }
}
