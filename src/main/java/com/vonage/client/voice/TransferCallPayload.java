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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.voice.ncco.Ncco;
import java.util.Objects;

/**
 * Extension of ModifyCallPayload which adds an NCCO destination to the serialized form.
 *
 */
class TransferCallPayload extends ModifyCallPayload {
    private final TransferDestination destination;

    public TransferCallPayload(String nccoUrl, String uuid) {
        super(ModifyCallAction.TRANSFER, uuid);
        destination = new TransferDestination(nccoUrl);
    }

    public TransferCallPayload(Ncco ncco, String uuid) {
        super(ModifyCallAction.TRANSFER, uuid);
        destination = new TransferDestination(Objects.requireNonNull(ncco, "NCCO is required."));
    }

    @JsonProperty("destination")
    public TransferDestination getDestination() {
        return destination;
    }
}
