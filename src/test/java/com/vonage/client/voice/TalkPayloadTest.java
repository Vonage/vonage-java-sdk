package com.vonage.client.voice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TalkPayloadTest {
    private TalkPayload payload;

    @Before
    public void setUp() throws Exception {
        payload = new TalkPayload("Hola Mundo!", TextToSpeechLanguage.AMERICAN_ENGLISH, 2, 0);
    }

    @Test
    public void getLoop() throws Exception {
        assertEquals(0, payload.getLoop());
    }

    @Test
    public void getText() throws Exception {
        assertEquals("Hola Mundo!", payload.getText());
    }

    @Test
    public void getLanguage() throws Exception {
        assertEquals(TextToSpeechLanguage.AMERICAN_ENGLISH, payload.getLanguage());
    }

    @Test
    public void getStyle() throws Exception {
        assertEquals(Integer.valueOf(2), payload.getStyle());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"text\":\"Hola Mundo!\",\"loop\":0,\"language\":\"en-US\",\"style\":2}";
        assertEquals(jsonString, payload.toJson());
    }
}
