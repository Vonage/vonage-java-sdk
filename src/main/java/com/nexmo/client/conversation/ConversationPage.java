package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationPage implements Iterable<Conversation> {
    private int page_size;
    private EmbeddedConvsersations conversations;
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
    public EmbeddedConvsersations getConversations() {
        return conversations;
    }

    @JsonProperty("_embedded")
    public void setConversations(EmbeddedConvsersations conversations) {
        this.conversations = conversations;
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
    public Iterator<Conversation> iterator() {
        return new ArrayIterator<>(conversations.getConversations());
    }

    public static ConversationPage fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, ConversationPage.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from ConversationPage object.", jpe);
        }
    }
}
