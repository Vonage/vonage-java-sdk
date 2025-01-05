/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.ChannelType;

/**
 * Base class for channels.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Messenger.class, name = "messenger"),
        @JsonSubTypes.Type(value = Mms.class, name = "mms"),
        @JsonSubTypes.Type(value = Pstn.class, names = {"phone", "pstn"}),
        @JsonSubTypes.Type(value = Sip.class, name = "sip"),
        @JsonSubTypes.Type(value = Sms.class, name = "sms"),
        @JsonSubTypes.Type(value = Vbc.class, name = "vbc"),
        @JsonSubTypes.Type(value = Viber.class, name = "viber"),
        @JsonSubTypes.Type(value = Websocket.class, name = "websocket"),
        @JsonSubTypes.Type(value = Whatsapp.class, name = "whatsapp"),
        @JsonSubTypes.Type(value = WhatsappVoice.class, name = "whatsapp-voice")
})
public abstract class Channel extends JsonableBaseObject {
    protected ChannelType type;

    /**
     * If the {@code type} field is present in JSON payload, returns the value as an enum.
     * Usually however, the type can be inferred from the class name.
     * This method is provided for completeness in cases where the field may be present.
     *
     * @return The channel type as an enum, or {@code null} if absent or not applicable in this context.
     *
     * @since 8.4.0
     */
    @JsonProperty("type")
    public ChannelType getType() {
        return type;
    }

    /**
     * Sets the {@link #getType()} based on this class's type. This is useful for some API
     * calls where the type information is expected to be present in the JSON.
     *
     * @since 8.4.0
     * @see #removeTypeField()
     */
    @JsonIgnore
    public void setTypeField() {
        String name = getClass().getSimpleName();
        if (name.equals("Pstn")) {
            name = "Phone";
        }
        if (name.equals("WhatsappVoice")) {
            name = "whatsapp-voice";
        }
        type = ChannelType.fromString(name);
    }

    /**
     * This method makes {@link #getType()} return {@code null}; effectively
     * the opposite of {@linkplain #setTypeField()}. This is useful for some API calls
     * where the type information should be omitted from the generated JSON.
     *
     * @since 8.4.0
     * @see #setTypeField()
     */
    public void removeTypeField() {
        type = null;
    }

    /**
     * Finds the corresponding subclass of Channel based on the enum's value.
     *
     * @param type The channel type as an enum.
     *
     * @return The appropriate concrete Channel class, or {@code null} if there isn't one.
     * @since 8.4.0
     */
    public static Class<? extends Channel> getConcreteClass(ChannelType type) {
        if (type == null) return null;
        switch (type) {
            default: return null;
            case MESSENGER: return Messenger.class;
            case MMS: return Mms.class;
            case PHONE: return Pstn.class;
            case SIP: return Sip.class;
            case SMS: return Sms.class;
            case VBC: return Vbc.class;
            case VIBER: return Viber.class;
            case WEBSOCKET: return Websocket.class;
            case WHATSAPP: return Whatsapp.class;
            case WHATSAPP_VOICE: return WhatsappVoice.class;
        }
    }
}
