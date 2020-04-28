package com.mateusfma.assemblyvoting.controller.rest.enums;

public enum VoteValue {
    YES(true),
    NO(false);

    private Boolean value;

    VoteValue(Boolean value) {
        this.value = value;
    }

    public static VoteValue fromValue(String value) {
        if (value == null)
            return null;
        else if (value.equals("Sim"))
            return YES;
        else if (value.equals("Não"))
            return NO;

        return null;
    }

    public Boolean getValue() {
        return value;
    }
}
