package com.nexmo.client;/*
 * Copyright (c) 2011-2016 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public byte[] loadKey(String path) throws IOException {
        int len;
        int size = 1024;
        byte[] buf = new byte[size];

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        InputStream is = this.getClass().getResourceAsStream(path);
        if (is != null) {
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
        } else {
            throw new IOException("Could not find resource at: " + this.getClass().getResource(path));
        }
        return bos.toByteArray();

    }
}
