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

public class Psd2Endpoint {

    public Psd2Method method;

    public Psd2Endpoint(HttpWrapper wrapper) {
        method = new Psd2Method(wrapper);
    }

    public VerifyResponse psd2Verify(String number, Double amount, String payee){
        return psd2Verify(new Psd2Request.Builder(number, amount, payee).build());
    }

    public VerifyResponse psd2Verify(String number, Double amount, String payee, Psd2Request.Workflow workflow){
        return psd2Verify(new Psd2Request.Builder(number, amount, payee).workflow(workflow).build());
    }

    public VerifyResponse psd2Verify(Psd2Request request){
        return method.execute(request);
    }
}
