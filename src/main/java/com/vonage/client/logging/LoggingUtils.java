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
package com.vonage.client.logging;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class LoggingUtils {

    public static String logResponse(HttpResponse response) throws IOException {
        StringBuilder log = new StringBuilder();
        String responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";
        int statusCode = response.getStatusLine().getStatusCode();
        String statusLine = response.getStatusLine().getReasonPhrase();

        log.append("status_code: ")
                .append(statusCode)
                .append( ", ")
                .append("status_line: ")
                .append(statusLine)
                .append(", ")
                .append(" body: ")
                .append(responseBody);

        //Calling Http#getEntity will consume the entity so you have to replace the entity after logging
        response.setEntity(new StringEntity(responseBody, ContentType.get(response.getEntity())));

        return log.toString();
    }
}
