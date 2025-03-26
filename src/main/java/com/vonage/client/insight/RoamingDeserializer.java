/*
 *   Copyright 2021 Vonage
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
package com.vonage.client.insight;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;

class RoamingDeserializer extends StdDeserializer<RoamingDetails> {

    RoamingDeserializer() {
        this(null);
    }

    RoamingDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RoamingDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        RoamingStatus status = RoamingStatus.UNKNOWN;
        String roamingCountryCode = null;
        String roamingNetworkCode = null;
        String roamingNetworkName = null;
        if (node.getNodeType() == JsonNodeType.STRING) {
            status = RoamingStatus.fromString(node.asText());
        }
        else if (node.getNodeType() == JsonNodeType.OBJECT) {
            status = RoamingStatus.fromString(node.get("status").asText());
            JsonNode rcc = node.get("roaming_country_code");
            if (rcc != null) {
                roamingCountryCode = rcc.asText();
            }
            JsonNode rnc = node.get("roaming_network_code");
            if (rnc != null) {
                roamingNetworkCode = rnc.asText();
            }
            JsonNode rnn = node.get("roaming_network_name");
            if (rnn != null) {
                roamingNetworkName = rnn.asText();
            }
        }
        return new RoamingDetails(status, roamingCountryCode, roamingNetworkCode, roamingNetworkName);
    }
}
