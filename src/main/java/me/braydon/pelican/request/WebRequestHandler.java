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
package me.braydon.pelican.request;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.braydon.pelican.client.ClientConfig;
import me.braydon.pelican.model.PanelModel;

/**
 * The handler for processing web requests.
 *
 * @author Braydon
 */
@AllArgsConstructor
public final class WebRequestHandler {
    /**
     * The client config used to make requests.
     */
    @NonNull private final ClientConfig clientConfig;

    /**
     * Handle the given web request.
     *
     * @param request the request to handle
     * @param responseType the expected response type, null if none
     * @return the response, null if none
     * @param <T> the response type
     */
    public <T extends PanelModel<T>> T handle(@NonNull JsonWebRequest request, Class<T> responseType) {
        // TODO: handle rate limit handling for async reqs
        return request.execute(clientConfig, responseType);
    }
}