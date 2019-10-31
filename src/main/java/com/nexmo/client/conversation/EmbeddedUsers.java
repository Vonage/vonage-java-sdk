package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedUsers {
    private User[] users;

    @JsonProperty("users")
    public User[] getUsers() {
        return users;
    }

    @JsonProperty("users")
    public void setUsers(User[] users) {
        this.users = users;
    }
}
