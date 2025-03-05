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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.whatsapp.Template;
import com.vonage.client.messages.whatsapp.Whatsapp;
import java.util.List;

public class WhatsappTemplate extends Template {
    private Whatsapp whatsapp;

    WhatsappTemplate() {}

    WhatsappTemplate(String name, List<String> parameters, Whatsapp whatsapp) {
        super(name, parameters);
        this.whatsapp = whatsapp;
    }

    @JsonProperty("whatsapp")
    public Whatsapp getWhatsapp() {
        return whatsapp;
    }
}
