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
package com.vonage.client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Tests for Java 8 deprecation warning functionality.
 * 
 * Note: The actual warning will only appear when running on Java 8.
 * This test verifies that the VonageClient can be instantiated without errors
 * and that the warning mechanism is properly integrated.
 */
public class Java8DeprecationWarningTest extends AbstractClientTest<VonageClient> {
    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testVonageClientInstantiatesWithWarningMechanism() throws Exception {
        // Capture System.err to check if warning would be triggered on Java 8
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
            VonageClient client = VonageClient.builder()
                    .applicationId("951614e0-eec4-4087-a6b1-3f4c2f169cb0")
                    .privateKeyContents(keyBytes)
                    .build();

            assertNotNull(client);
            
            // The warning will only appear if running on Java 8 (version "1.8")
            String javaVersion = System.getProperty("java.specification.version");
            if ("1.8".equals(javaVersion)) {
                String output = errContent.toString();
                assertTrue(output.contains("[VONAGE SDK WARNING]"), 
                    "Expected Java 8 deprecation warning on Java 8");
                assertTrue(output.contains("June 30, 2026"), 
                    "Warning should mention the deprecation date");
            }
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testMultipleInstantiationsOnlyWarnOnce() throws Exception {
        // Capture System.err
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
            
            // Create multiple clients
            VonageClient client1 = VonageClient.builder()
                    .applicationId("951614e0-eec4-4087-a6b1-3f4c2f169cb0")
                    .privateKeyContents(keyBytes)
                    .build();
                    
            VonageClient client2 = VonageClient.builder()
                    .applicationId("951614e0-eec4-4087-a6b1-3f4c2f169cb0")
                    .privateKeyContents(keyBytes)
                    .build();

            assertNotNull(client1);
            assertNotNull(client2);
            
            // On Java 8, the warning should only appear once
            String javaVersion = System.getProperty("java.specification.version");
            if ("1.8".equals(javaVersion)) {
                String output = errContent.toString();
                int warningCount = output.split("\\[VONAGE SDK WARNING\\]", -1).length - 1;
                assertEquals(1, warningCount, 
                    "Warning should only appear once despite multiple instantiations");
            }
        } finally {
            System.setErr(originalErr);
        }
    }
}
