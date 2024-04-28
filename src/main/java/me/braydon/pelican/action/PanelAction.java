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
package me.braydon.pelican.action;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.braydon.pelican.model.PanelModel;
import me.braydon.pelican.request.JsonWebRequest;
import me.braydon.pelican.request.WebRequestHandler;

/**
 * An action that can be executed on a panel.
 *
 * @param <T> the type of response expected when this action is executed
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PanelAction<T extends PanelModel<T>> {
    /**
     * The web handler to execute actions with.
     */
    @NonNull private final WebRequestHandler requestHandler;

    /**
     * The web request this action will execute.
     */
    @NonNull private final JsonWebRequest webRequest;

    /**
     * The type of response expected when
     * this action is executed, null if none.
     */
    private final Class<T> responseType;

    /**
     * Execute this action instantly.
     *
     * @return the response, null if none
     */
    public T execute() {
        return requestHandler.handle(webRequest, responseType);
    }

    /**
     * Create a new panel action.
     *
     * @param requestHandler the request handler to use
     * @param webRequest     the web request to send for this action
     * @param responseType   the expected response type, null if none
     * @param <T>            the response type
     * @return the panel action
     */
    @NonNull
    public static <T extends PanelModel<T>> PanelAction<T> create(@NonNull WebRequestHandler requestHandler,
                                                                  @NonNull JsonWebRequest webRequest, Class<T> responseType) {
        return new PanelAction<>(requestHandler, webRequest, responseType);
    }
}