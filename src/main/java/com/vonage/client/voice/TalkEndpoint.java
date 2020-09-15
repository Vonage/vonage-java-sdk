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

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClientException;

public class TalkEndpoint {
    private final StartTalkMethod startTalk;
    private final StopTalkMethod stopTalk;

    public TalkEndpoint(HttpWrapper wrapper) {
        this.startTalk = new StartTalkMethod(wrapper);
        this.stopTalk = new StopTalkMethod(wrapper);
    }

    public TalkResponse put(TalkRequest request) throws VonageClientException {
        return this.startTalk.execute(request);
    }

    public TalkResponse delete(String uuid) throws VonageClientException {
        return this.stopTalk.execute(uuid);
    }
}