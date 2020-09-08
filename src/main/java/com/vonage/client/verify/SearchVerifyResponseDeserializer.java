/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.vonage.client.verify;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;

public class SearchVerifyResponseDeserializer extends JsonDeserializer<SearchVerifyResponse> {
    @Override
    public SearchVerifyResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        // TODO: Restructure objects to handle this better.
        // Deserialization is a little complicated here.  There are a few things to consider:
        // (1) A search request with a single result comes back with that single result as json.
        // (2) A search request with multiple results comes back with those results as an array in the
        //     verification_requests property.
        // (3) A search request which comes back in error has different Status values than search requests
        //     that come back without error. (See VerifyStatus vs VerifyDetails.Status)

        // If the results has a verification_requests node then we can successfully map our object as normal.
        if (node.has("verification_requests")) {
            // Have to create a second object mapper to handle this as we want to bypass custom deserialization.
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return mapper.readValue(node.toString(), SearchVerifyResponse.class);
        }

        // If the result has error_text, we can assume that the only fields that matter are status and the error.
        if (node.has("error_text")) {
            return new SearchVerifyResponse(VerifyStatus.fromInt(node.get("status").asInt()),
                                            node.get("error_text").asText());
        }
        // Otherwise we need to map the single result and then put it on the list as is.
        VerifyDetails details = p.getCodec().treeToValue(node, VerifyDetails.class);
        return new SearchVerifyResponse(Collections.singletonList(details));
    }
}
