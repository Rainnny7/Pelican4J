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
import lombok.Getter;
import lombok.NonNull;
import me.braydon.pelican.client.ClientConfig;
import me.braydon.pelican.model.PanelModel;
import me.braydon.pelican.request.JsonWebRequest;
import me.braydon.pelican.request.RateLimitHandler;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An action that can be executed on a panel.
 *
 * @param <T> the type of response expected when this action is executed
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PanelAction<T extends PanelModel<T>> {
    /**
     * The client config to use for executing actions.
     */
    @NonNull private final ClientConfig clientConfig;

    /**
     * The rate limit handler to use for querying actions.
     */
    @NonNull private final RateLimitHandler rateLimitHandler;

    /**
     * The web request this action will execute.
     */
    @Getter @NonNull private final JsonWebRequest webRequest;

    /**
     * The type of response expected when
     * this action is executed, null if none.
     */
    private final Class<T> responseType;

    /**
     * Queue this action and await its response.
     * The given callback will be invoked once
     * a response has been received from the panel.
     *
     * @param callback the callback to invoke
     */
    public void queue(@NonNull Consumer<T> callback) {
        queue((res, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }
            callback.accept(res);
        });
    }

    /**
     * Queue this action and await its response.
     * The given callback will be invoked once
     * a response has been received from the panel.
     *
     * @param callback the callback to invoke
     */
    public void queue(@NonNull BiConsumer<T, Exception> callback) {
        CompletableFuture.runAsync(() -> rateLimitHandler.tryRequest(this, callback, false, true));
    }

    /**
     * Execute this action instantly.
     *
     * @return the response, null if none
     */
    public T execute() {
        return webRequest.execute(clientConfig, responseType);
    }

    /**
     * Create a new panel action.
     *
     * @param clientConfig   the client config to use for executing actions
     * @param rateLimitHandler the rate limit handler to use for querying actions
     * @param webRequest     the web request to send for this action
     * @param responseType   the expected response type, null if none
     * @param <T>            the response type
     * @return the panel action
     */
    @NonNull
    public static <T extends PanelModel<T>> PanelAction<T> create(@NonNull ClientConfig clientConfig,
                                                                  @NonNull RateLimitHandler rateLimitHandler,
                                                                  @NonNull JsonWebRequest webRequest, Class<T> responseType) {
        return new PanelAction<>(clientConfig, rateLimitHandler, webRequest, responseType);
    }
}