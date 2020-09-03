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
package com.nexmo.client.voice.ncco;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class InputActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        InputAction.Builder builder = InputAction.builder();
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        InputAction input = InputAction.builder()
                .timeOut(10)
                .maxDigits(4)
                .submitOnHash(true)
                .eventUrl("http://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"timeOut\":10,\"maxDigits\":4,\"submitOnHash\":true,\"eventUrl\":[\"http://example.com\"],\"eventMethod\":\"POST\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testGetAction() {
        InputAction input = InputAction.builder().build();
        assertEquals("input", input.getAction());
    }

    @Test
    public void testDefault() {
        InputAction input = InputAction.builder().build();

        String expectedJson = "[{\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testTimeOutField() {
        InputAction input = InputAction.builder().timeOut(5).build();

        String expectedJson = "[{\"timeOut\":5,\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testMaxDigitsField() {
        InputAction input = InputAction.builder().maxDigits(4).build();

        String expectedJson = "[{\"maxDigits\":4,\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testSubmitOnHashField() {
        InputAction input = InputAction.builder().submitOnHash(true).build();

        String expectedJson = "[{\"submitOnHash\":true,\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testEventUrlField() {
        InputAction input = InputAction.builder().eventUrl("http://example.com").build();

        String expectedJson = "[{\"eventUrl\":[\"http://example.com\"],\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testEventMethodField() {
        InputAction input = InputAction.builder().eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"eventMethod\":\"POST\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }
}
