package com.nexmo.client.voice;/*
 * Copyright (c) 2011-2017 Nexmo Inc
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

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoiceName {
    Salli,
    Joey,
    Naja,
    Mads,
    Marlene,
    Hans,
    Nicole,
    Russell,
    Amy,
    Brian,
    Emma,
    Gwyneth,
    Geraint,
    Raveena,
    Chipmunk,
    Eric,
    Ivy,
    Jennifer,
    Justin,
    Kendra,
    Kimberly,
    Conchita,
    Enrique,
    Penelope,
    Miguel,
    Chantal,
    Celine,
    Mathieu,
    Dora,
    Karl,
    Carla,
    Giorgio,
    Liv,
    Lotte,
    Ruben,
    Agnieszka,
    Jacek,
    Ewa,
    Jan,
    Maja,
    Vitoria,
    Ricardo,
    Cristiano,
    Ines,
    Carmen,
    Maxim,
    Tatyana,
    Astrid,
    Filiz;

    @JsonCreator
    public static VoiceName fromString(String name) {
        return VoiceName.valueOf(name);
    }

}
