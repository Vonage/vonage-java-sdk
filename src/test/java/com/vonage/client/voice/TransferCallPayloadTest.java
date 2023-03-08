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
package com.vonage.client.voice;/*
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
import com.vonage.client.voice.ncco.Ncco;
import com.vonage.client.voice.ncco.RecordAction;
import com.vonage.client.voice.ncco.TalkAction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TransferCallPayloadTest {
    @Test
    public void transferJson() throws Exception {
        String expected = "{\"action\":\"transfer\",\"destination\":{\"type\":\"ncco\",\"url\":[\"https://example"
                + ".com/ncco\"]}}";
        String actual = CallModifier.transferCall("not-a-uuid", "https://example.com/ncco").toJson();
        assertEquals(expected, actual);
    }

    @Test
    public void testTransferWithInlineNcco() {
        String expected = "{\"action\":\"transfer\",\"destination\":{\"type\":\"ncco\",\"ncco\":[{\"text\":\"Hello from Vonage\",\"action\":\"talk\"},{\"action\":\"record\"},{\"text\":\"Thank you!\",\"action\":\"talk\"}]}}";
        String actual = CallModifier.transferCall("not-a-uuid", new Ncco(TalkAction.builder("Hello from Vonage").build(),
                RecordAction.builder().build(),
                TalkAction.builder("Thank you!").build())
        ).toJson();

        assertEquals(expected, actual);
    }

    @Test
    public void testTypeValueOf() throws Exception {
        assertEquals(TransferDestination.Type.NCCO, TransferDestination.Type.valueOf("NCCO"));
    }
}
