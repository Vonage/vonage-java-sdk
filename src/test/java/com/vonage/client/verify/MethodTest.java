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
package com.vonage.client.verify;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class MethodTest<T> {
    protected T method;

    protected void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue("" + params + " should contain " + item, params.contains(item));
    }

    protected void assertParamMissing(List<NameValuePair> params, String key) {
        Set<String> keys = new HashSet<>();
        for (NameValuePair pair : params) {
            keys.add(pair.getName());
        }
        assertFalse("" + params + " should not contain " + key, keys.contains(key));
    }


}
