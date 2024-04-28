/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.pelican.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A configuration for a {@link Pelican4J} client.
 *
 * @author Braydon
 */
@Builder @Getter @Accessors(fluent = true) @ToString
public final class ClientConfig {
    /**
     * The URL to the panel.
     */
    @NonNull private final String panelUrl;

    /**
     * The API key to use for authentication.
     */
    @ToString.Exclude @NonNull private final String apiKey;

    /**
     * Whether debugging should be enabled.
     */
    private final boolean debugging;

    /**
     * The config builder.
     */
    public static class ClientConfigBuilder {
        @NonNull
        public ClientConfigBuilder debugging(boolean state) {
            debugging = state;
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug"); // Enable debugging in the logger
            return this;
        }
    }
}