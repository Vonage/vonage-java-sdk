/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.sns;

import com.vonage.client.ClientTest;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sns.request.SnsPublishRequest;
import com.vonage.client.sns.request.SnsSubscribeRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnsClientTest extends ClientTest<SnsClient> {

    public SnsClientTest() {
        client = new SnsClient(wrapper);
    }

    @Test
    public void testSubscribe() throws Exception {
        stubResponse(200, "<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "</nexmo-sns>"
        );
        SnsSubscribeResponse result = client.subscribe(new SnsSubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testPublish() throws Exception {
        stubResponse(200, "<nexmo-sns>\n" +
                "   <command>publish</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>"
        );
        SnsPublishResponse result = client.publish(new SnsPublishRequest(
                "arn:aws:sns:region:num:id",
                "447777111222",
                "447777111223",
                "Hello!"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testSubmitWithInvalidResponse() throws Exception {
        stubResponse(500, "<nexmo-sns/>");
        assertThrows(VonageResponseParseException.class, () -> client.subscribe(new SnsSubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        )));
    }
}
