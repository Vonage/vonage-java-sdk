/*
 *   Copyright 2023 Vonage
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

import org.apache.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a recording obtained from {@link VoiceClient#downloadRecording(String)}.
 */
public class Recording {
    private final HttpResponse response;

    public Recording(HttpResponse response) {
        this.response = response;
    }

    /**
     * Gets the data stream from the HTTP response.
     *
     * @return The raw data from the response.
     *
     * @throws IOException If the InputStream could not be created.
     */
    public InputStream getContent() throws IOException {
        return response.getEntity().getContent();
    }

    /**
     * Saves the downloaded recording to a file.
     *
     * @param path The path to save the recording to.
     *
     * @throws IOException If there was an error writing the file.
     */
    public void save(String path) throws IOException {
        save(FileSystems.getDefault().getPath(path));
    }

    /**
     * Saves the downloaded recording to a file.
     *
     * @param path The file to save the recording to.
     *
     * @throws IOException If there was an error writing the file.
     */
    public void save(Path path) throws IOException {
        Files.copy(getContent(), path);
    }
}
