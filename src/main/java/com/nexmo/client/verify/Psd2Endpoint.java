package com.nexmo.client.verify;

import com.nexmo.client.HttpWrapper;

public class Psd2Endpoint {

    public Psd2Method method;

    public Psd2Endpoint(HttpWrapper wrapper) {
        this.method = new Psd2Method(wrapper);
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
