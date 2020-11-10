/*
 * Copyright (c) 2020  Vonage 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vonage.client.application;

import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class AppBasicAuthTest {

    @Test
    public void testApplyAuth() throws Exception {
        String expectedAuthStr = "Authorization: Basic NHQ4YWVhZ2I6WHlNczJKa21BMVp6OWRVaw==";
        HttpWrapper wrapper = new HttpWrapper(new TokenAuthMethod("4t8aeagb", "XyMs2JkmA1Zz9dUk"));
        CreateApplicationMethod method = new CreateApplicationMethod(wrapper);
        RequestBuilder request = method.makeRequest(Application.builder().build());
        RequestBuilder requestWithAuth = method.applyAuth(request);

        assertEquals(expectedAuthStr, String.valueOf(requestWithAuth.getHeaders("Authorization")[0]));

    }
}
