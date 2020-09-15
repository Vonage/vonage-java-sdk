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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Recording {
    private HttpResponse response;

    public Recording(HttpResponse response) {
        this.response = response;
    }

    public InputStream getContent() throws IOException {
        return this.response.getEntity().getContent();
    }

    public void save(String path) throws IOException {
        this.save(FileSystems.getDefault().getPath(path));
    }

    public void save(Path path) throws IOException {
        try (OutputStream out = new FileOutputStream(path.toFile())) {
            IOUtils.copy(this.response.getEntity().getContent(), out);
        }
    }
}
