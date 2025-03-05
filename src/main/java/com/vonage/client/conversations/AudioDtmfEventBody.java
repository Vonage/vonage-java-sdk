package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Main body container for {@link AudioDtmfEvent}.
 *
 * @since 8.19.0
 */
class AudioDtmfEventBody extends JsonableBaseObject {
    @JsonAlias({"digits", "digit"}) String digits;
    @JsonProperty("dtmf_seq") Integer dtmfSeq;
    @JsonProperty("channel") MemberChannel channel;

    AudioDtmfEventBody() {}

    AudioDtmfEventBody(AudioDtmfEvent.Builder builder) {
        digits = builder.digits;
        dtmfSeq = builder.dtmfSeq;
        channel = builder.channel;
    }
}
