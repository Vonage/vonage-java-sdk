package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;
import java.util.Iterator;

public class MemberPage implements Iterable<Member> {
    private int page_size;
    private EmbeddedMembers members;
    private PageLinks pageLinks;

    @JsonProperty("page_size")
    public int getPage_size() {
        return page_size;
    }

    @JsonProperty("page_size")
    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    @JsonProperty("_embedded")
    public EmbeddedMembers getMembers() {
        return members;
    }

    @JsonProperty("_embedded")
    public void setMembers(EmbeddedMembers members) {
        this.members = members;
    }

    @JsonProperty("_links")
    public PageLinks getPageLinks() {
        return pageLinks;
    }

    @JsonProperty("_links")
    public void setPageLinks(PageLinks pageLinks) {
        this.pageLinks = pageLinks;
    }

    @Override
    public Iterator<Member> iterator() {
        return new ArrayIterator<>(members.getMembers());
    }

    public static MemberPage fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, MemberPage.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from MemberPage object.", jpe);
        }
    }
}
