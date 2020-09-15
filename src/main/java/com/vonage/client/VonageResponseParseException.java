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
package com.vonage.client;


/**
 * An exception that indicates the contents of an HttpResponse could not be parsed.
 */
public class VonageResponseParseException extends VonageUnexpectedException {
    public VonageResponseParseException(String message) {
        this(message, null);
    }

    public VonageResponseParseException(String message, Throwable t) {
        super(message, t);
    }
}
