/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @param <T> DTMF now returns a object that contains the digits the user pressed as well as whether the DTMF input timed
 *           out. The API still returns just the digits as a string if the NCCO request was sent with the old DTMF settings
 *           input in the input action. For backwards compatibility use {@link String} if you sent NCCO request with the
 *           old DTMF settings. Use {@link DtmfResult} if the Input Action used the current
 *           <a href="https://developer.nexmo.com/voice/voice-api/ncco-reference#dtmf-input-settings">DTMF Settings</a> in
 *           the documentation.
 * @since 5.6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputEvent<T> {
    private String uuid;
    private String conversationUuid;
    private Boolean timedOut;
    private T dtmf;
    private Date timestamp;
    private String to;
    private String from;
    private SpeechResults speech;

    /**
     * @return The unique identifier for this call
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return The unique identifier for this conversation
     */
    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    /**
     *Deprecated. Use {@link DtmfResult#isTimedOut()}
     * @return Whether the DTMF input timed out: true if it did, false if not.
     */
    @JsonProperty("timed_out")
    @Deprecated
    public boolean isTimedOut() {
        return timedOut;
    }

    /**
     * @return DTMF capturing retults.
     */
    public T getDtmf() {
        return dtmf;
    }

    /**
     * @return Timestamp (ISO 8601 format)
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @return The number the call was made to
     */
    public String getTo(){
        return to;
    }

    /**
     * @return The number the call came from
     */
    public String getFrom(){
        return from;
    }

    /**
     * @return Speech recognition results
     */
    public SpeechResults getSpeech() {
        return speech;
    }

    public static <T> T fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dtmfNode = mapper.readTree(json).get("dtmf");

            //determine if json string contains new dtmf object or old dtmf string.
            if (dtmfNode != null && !dtmfNode.isTextual()){
                return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(InputEvent.class, DtmfResult.class));
            } else {
                return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(InputEvent.class, String.class));
            }
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce InputEvent from json.", jpe);
        }
    }
}
