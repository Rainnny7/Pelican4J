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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.braydon.pelican.action.PanelAction;
import me.braydon.pelican.client.ClientConfig;
import me.braydon.pelican.exception.PanelAPIException;
import me.braydon.pelican.model.PanelModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Responsible for handling panel rate limits.
 *
 * @author Braydon
 */
@Slf4j(topic = "Rate Limit Handler")
public final class RateLimitHandler {
    /**
     * The interval at which requests should be retried at if they are rate limited.
     */
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2L);

    /**
     * The amount of times a queued action should
     * be retried before failing the action.
     */
    private static final int MAX_RETRIES = 25;

    /**
     * The client config to use.
     */
    @NonNull private final ClientConfig clientConfig;

    /**
     * The currently queued actions, awaiting to be re-tried.
     */
    private final List<QueuedAction<?>> queuedActions = new LinkedList<>();

    public RateLimitHandler(@NonNull ClientConfig clientConfig) {
        this.clientConfig = clientConfig;

        // Schedule a task to process the queue.
        // This will take the first element in the queue
        // every X interval, and try to execute it. Each
        // time the action fails, the retry count will be
        // incremented. If the action is successful, or reaches
        // the max retries, it will be removed from the queue.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int queueSize = queuedActions.size(); // The size of the queue
                if (queueSize == 0) { // Queue is empty
                    return;
                }
                if (clientConfig.debugging()) {
                    log.debug("Processing queue of size {}...", queueSize);
                }
                QueuedAction<?> queuedAction = queuedActions.get(0); // Get the first queued action
                boolean maxedRetries = queuedAction.getRetries() >= MAX_RETRIES; // If the action has maxed retries

                // Re-try the queued action, and if it fails, increment the retry count and move on
                if (!tryRequest(queuedAction.getAction(), queuedAction.getCallback(), maxedRetries, false)) {
                    queuedAction.incrementRetries();
                    if (!maxedRetries) {
                        return;
                    }
                }
                // The queued action was either successful, or
                // reached the max retries, remove it from the queue
                queuedActions.remove(0);
            }
        }, INTERVAL, INTERVAL);
    }

    /**
     * Try and execute the given action.
     * <p>
     * Doing this will try and execute the
     * given action and immediately respond
     * with the result. If the action fails
     * to execute, and the error is due to
     * a rate limit, the action will be queued
     * to be re-tried later. All other errors
     * will be passed to the given callback.
     * </p>
     *
     * @param action the action to try
     * @param callback the callback to invoke
     * @param throwRateLimitErrors whether rate limit errors should be thrown
     * @param retry should the action be retried if a rate limit is hit (and being thrown)?
     * @param <T> the action response type
     * @return whether the request was successful
     */
    @SuppressWarnings("unchecked")
    public <T extends PanelModel<T>> boolean tryRequest(@NonNull PanelAction<?> action, BiConsumer<T, Exception> callback, boolean throwRateLimitErrors, boolean retry) {
        try { // Try and execute the request
            if (clientConfig.debugging()) {
                log.debug("Trying to execute action {}...", action.getWebRequest());
            }
            callback.accept((T) action.execute(), null);
            return true; // Success
        } catch (Exception ex) {
            // The API rate limit has been reached, queue
            // the task to be re-tried later
            if (ex instanceof PanelAPIException) {
                PanelAPIException apiException = (PanelAPIException) ex;
                if (apiException.getCode() == HttpStatus.TOO_MANY_REQUESTS && !throwRateLimitErrors) {
                    if (clientConfig.debugging()) {
                        log.debug("Panel API rate limit exceeded{}", retry ? ", queued action to be re-tried later..." : "");
                    }
                    if (retry) {
                        queuedActions.add(new QueuedAction<>(action, callback, 0));
                    }
                    return false;
                }
            }
            // Not a rate limit, invoke
            // the callback with the error
            if (clientConfig.debugging()) {
                log.debug("Failed to execute action:", ex);
            }
            callback.accept(null, ex);
            return false;
        }
    }
}