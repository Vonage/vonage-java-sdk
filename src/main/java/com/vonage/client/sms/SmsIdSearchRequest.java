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
package com.vonage.client.sms;

import org.apache.http.client.methods.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class SmsIdSearchRequest implements SearchSmsRequest {
    private final List<String> ids;

    public SmsIdSearchRequest(String id) {
        this(new ArrayList<>(10));
        ids.add(id);
    }

    public SmsIdSearchRequest(List<String> ids) {
        this.ids = new ArrayList<>(ids);
    }

    public void addId(String id) {
        ids.add(id);
    }

    public void addParams(RequestBuilder request) {
        for (String id : ids) {
            request.addParameter("ids", id);
        }

    }
}
