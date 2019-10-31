package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageLinks {
    private PageLink first;
    private PageLink self;
    private PageLink next;
    private PageLink prev;

    @JsonProperty("first")
    public PageLink getFirst() {
        return first;
    }

    @JsonProperty("first")
    public void setFirst(PageLink first) {
        this.first = first;
    }

    @JsonProperty("self")
    public PageLink getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(PageLink self) {
        this.self = self;
    }

    @JsonProperty("next")
    public PageLink getNext() {
        return next;
    }

    @JsonProperty("next")
    public void setNext(PageLink next) {
        this.next = next;
    }

    @JsonProperty("prev")
    public PageLink getPrev() {
        return prev;
    }

    @JsonProperty("prev")
    public void setPrev(PageLink prev) {
        this.prev = prev;
    }
}
