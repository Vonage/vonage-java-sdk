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
package com.vonage.client.application;

import com.vonage.client.Jsonable;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationListTest {

    @Test
    public void testEmptyApplications() {
        var al = Jsonable.fromJson("{\"_embedded\":{\"applications\":[]}}", ApplicationList.class);
        assertNotNull(al);
        var applications = al.getApplications();
        assertNotNull(applications);
        assertEquals(0, applications.size());
    }

    @Test
    public void testNoApplications() {
        var al = Jsonable.fromJson("{\"_embedded\":{}}", ApplicationList.class);
        assertNotNull(al);
        assertNull(al.getApplications());
    }

    @Test
    public void testEmptyJson() {
        var al = Jsonable.fromJson("{}", ApplicationList.class);
        assertNotNull(al);
        assertNull(al.getApplications());
    }
}
