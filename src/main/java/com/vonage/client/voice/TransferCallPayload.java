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
package com.vonage.client.voice;

import com.vonage.client.voice.ncco.Ncco;

/**
 * Extension of ModifyCallPayload which adds an NCCO destination to the serialized form.
 */
public class TransferCallPayload extends ModifyCallPayload {
    private String nccoUrl;
    private Ncco ncco;

    public TransferCallPayload(String nccoUrl) {
        super(ModifyCallAction.TRANSFER);
        this.nccoUrl = nccoUrl;
    }

    public TransferCallPayload(Ncco ncco) {
        super(ModifyCallAction.TRANSFER);
        this.ncco = ncco;
    }

    public TransferDestination getDestination() {
        return new TransferDestination(TransferDestination.Type.NCCO, this.nccoUrl, this.ncco);
    }
}
