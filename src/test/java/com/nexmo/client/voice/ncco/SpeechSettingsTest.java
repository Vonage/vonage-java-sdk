package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SpeechSettingsTest {

    @Test
    public void getLanguage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        String jsonStr = mapper.writeValueAsString(SpeechSettings.Language.ENGLISH_AUSTRALIA);
        String jsonStrToObject = "\"Invalid Language\"";
        SpeechSettings.Language language = mapper.readValue(jsonStrToObject, SpeechSettings.Language.class);

        System.out.println(jsonStr);
        System.out.println(language);

        assertEquals(SpeechSettings.Language.UNKNOWN, language);
    }
}