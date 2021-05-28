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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;

public class RoamingDeseriazlizer extends StdDeserializer<RoamingDetails> {
    public RoamingDeseriazlizer(){
        this(null);
    }

    @Override
    public RoamingDetails deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        RoamingDetails.RoamingStatus status = RoamingDetails.RoamingStatus.UNKNOWN;
        String roamingCountryCode = null;
        String roamingNetworkCode = null;
        String roamingNetworkName = null;
        if (node.getNodeType() == JsonNodeType.STRING){
            status = RoamingDetails.RoamingStatus.fromString(node.asText());
        }
        else if(node.getNodeType() == JsonNodeType.OBJECT){
            status = RoamingDetails.RoamingStatus.fromString(node.get("status").asText());
            if(!(node.get("roaming_country_code") == null)){
                roamingCountryCode = node.get("roaming_country_code").asText();
            }
            if(!(node.get("roaming_network_code") == null)){
                roamingNetworkCode = node.get("roaming_network_code").asText();
            }
            if(!(node.get("roaming_network_name") == null)){
                roamingNetworkName = node.get("roaming_network_name").asText();
            }
        }
        RoamingDetails details = new RoamingDetails(status,roamingCountryCode,roamingNetworkCode,roamingNetworkName);
        return details;
    }

    public RoamingDeseriazlizer(Class<?> vc){
        super(vc);
    }


}
