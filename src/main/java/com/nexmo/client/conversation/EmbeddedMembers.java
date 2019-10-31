package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedMembers {
    private Member[] members;

    @JsonProperty("members")
    public Member[] getMembers() {
        return members;
    }

    @JsonProperty("members")
    public void setMembers(Member[] members) {
        this.members = members;
    }
}
