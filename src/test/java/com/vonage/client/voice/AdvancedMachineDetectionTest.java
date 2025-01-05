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
package com.vonage.client.voice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Map;

public class AdvancedMachineDetectionTest {

    @Test
    public void testSerializeAndParseAllParams() throws Exception {
        AdvancedMachineDetection amd = AdvancedMachineDetection.builder()
                .behavior(MachineDetection.HANGUP)
                .mode(AdvancedMachineDetection.Mode.DETECT)
                .beepTimeout(90)
                .build();

        TestUtils.testJsonableBaseObject(amd);
        Map<String, ?> map = new ObjectMapper().readValue(amd.toJson(), new TypeReference<Map<String, ?>>(){});
        assertEquals(3, map.size());
        assertEquals("hangup", map.get("behavior"));
        assertEquals("detect", map.get("mode"));
        assertEquals(90, map.get("beep_timeout"));
    }

    @Test
    public void testConstructRequiredParams() {
        AdvancedMachineDetection amd = AdvancedMachineDetection.builder().build();
        TestUtils.testJsonableBaseObject(amd);
        assertNull(amd.getBehavior());
        assertNull(amd.getMode());
        assertNull(amd.getBeepTimeout());
    }

    @Test
    public void testBeepTimeoutBoundaries() {
        int min = 45, max = 120;
        assertEquals(min, AdvancedMachineDetection.builder().beepTimeout(min).build().getBeepTimeout().intValue());
        assertEquals(max, AdvancedMachineDetection.builder().beepTimeout(max).build().getBeepTimeout().intValue());
        assertThrows(IllegalArgumentException.class, () ->
                AdvancedMachineDetection.builder().beepTimeout(min-1).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                AdvancedMachineDetection.builder().beepTimeout(max+1).build()
        );
    }

    @Test
    public void testInvalidMode() {
        assertNull(AdvancedMachineDetection.Mode.fromString("boop"));
    }
}
