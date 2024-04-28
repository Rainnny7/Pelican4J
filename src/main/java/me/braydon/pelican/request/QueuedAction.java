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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.pelican.action.PanelAction;
import me.braydon.pelican.model.PanelModel;

import java.util.function.BiConsumer;

/**
 * Represents a queued action.
 *
 * @author Braydon
 * @param <T> the action response type
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public class QueuedAction<T extends PanelModel<T>> {
    /**
     * The action that's queued.
     */
    @NonNull private final PanelAction<?> action;

    /**
     * The callback to invoke when
     * this action is executed.
     */
    @NonNull private final BiConsumer<T, Exception> callback;

    /**
     * The amount of times this
     * action has been retried.
     */
    private int retries;

    /**
     * Increment the retry count.
     */
    protected void incrementRetries() {
        retries++;
    }
}