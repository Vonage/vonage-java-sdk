/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ListApplicationRequestTest {

    @Test
    public void testPageSizeBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                ListApplicationRequest.builder().pageSize(1001).build()
        );

        assertEquals(1000, ListApplicationRequest.builder().pageSize(1000).build().getPageSize());

        assertEquals(1, ListApplicationRequest.builder().pageSize(1).build().getPageSize());

        assertThrows(IllegalArgumentException.class, () ->
                ListApplicationRequest.builder().pageSize(0).build()
        );
    }

    @Test
    public void testPageBounds() {
        assertThrows(IllegalArgumentException.class, () ->
                ListApplicationRequest.builder().page(0).build()
        );
        assertEquals(1, ListApplicationRequest.builder().page(1).build().getPage());
    }

    @Test
    public void testDefaultParameters() {
        var request = ListApplicationRequest.builder().build();
        assertNull(request.getPage());
        assertNull(request.getPageSize());
    }
}
