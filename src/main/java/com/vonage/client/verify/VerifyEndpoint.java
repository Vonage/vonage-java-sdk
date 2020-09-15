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

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClientException;

import java.util.Locale;

class VerifyEndpoint {
    private VerifyMethod verifyMethod;

    VerifyEndpoint(HttpWrapper httpWrapper) {
        this.verifyMethod = new VerifyMethod(httpWrapper);
    }

    VerifyResponse verify(String number, String brand, String from, int length, Locale locale, VerifyRequest.LineType type) throws VonageClientException {
        return verify(new VerifyRequest.Builder(number, brand)
                .senderId(from)
                .locale(locale)
                .type(type)
                .build()
        );
    }

    VerifyResponse verify(String number, String brand, String from, int length, Locale locale) throws VonageClientException {
        return verify(new VerifyRequest.Builder(number, brand)
                .senderId(from)
                .locale(locale)
                .build()
        );
    }

    VerifyResponse verify(String number, String brand, String from) throws VonageClientException {
        return verify(new VerifyRequest.Builder(number, brand)
                .senderId(from)
                .build()
        );
    }

    VerifyResponse verify(String number, String brand) throws VonageClientException {
        return verify(new VerifyRequest.Builder(number, brand).build());
    }

    VerifyResponse verify(String number, String brand, VerifyRequest.Workflow workflow) throws VonageClientException {
        return verify(
                new VerifyRequest.Builder(number, brand)
                        .workflow(workflow)
                        .build()
        );
    }

    VerifyResponse verify(VerifyRequest request) throws VonageClientException {
        return this.verifyMethod.execute(request);
    }
}
