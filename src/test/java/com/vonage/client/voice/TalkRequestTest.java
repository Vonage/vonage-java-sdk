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

package com.vonage.client.voice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TalkRequestTest {
    private TalkRequest request;

    @Before
    public void setUp() throws Exception {
        request = new TalkRequest("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "Wubba lubba dub dub", VoiceName.BRIAN, 0);
    }

    @Test
    public void testUuid() throws Exception {
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", request.getUuid());
        request.setUuid("abc");
        assertEquals("abc", request.getUuid());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"text\":\"Wubba lubba dub dub\",\"loop\":0,\"voice_name\":\"Brian\"}";
        assertEquals(jsonString, request.toJson());
    }

}
