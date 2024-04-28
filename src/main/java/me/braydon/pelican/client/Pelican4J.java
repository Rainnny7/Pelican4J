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

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import me.braydon.pelican.action.PanelActions;
import me.braydon.pelican.action.pelican.PelicanPanelActions;
import me.braydon.pelican.action.pterodactyl.PteroPanelActions;
import me.braydon.pelican.request.RateLimitHandler;

import java.io.Closeable;

/**
 * A client for the API.
 *
 * @author Braydon
 * @param <A> the actions for this client
 */
@Getter @Accessors(fluent = true)
@Slf4j(topic = "Pelican4J Client")
public final class Pelican4J<A extends PanelActions> implements Closeable {
    /**
     * The config for this client.
     */
    @NonNull private final ClientConfig config;

    /**
     * The actions for this client.
     */
    @NonNull private final A actions;

    @SneakyThrows
    private Pelican4J(@NonNull ClientConfig config, @NonNull Class<A> actionsClass) {
        this.config = config;
        actions = actionsClass.getConstructor(ClientConfig.class, RateLimitHandler.class).newInstance(
                config, new RateLimitHandler(config)
        );
        if (config.debugging()) {
            log.debug("Created a new {} client: {}", actionsClass == PelicanPanelActions.class ? "Pelican" : "Ptero", config);
        }
    }

    /**
     * Cleanup this client and shutdown.
     */
    @Override
    public void close() {
        // TODO: close any resources, e.g: websockets
    }

    /**
     * Create a new API client
     * for the Pelican panel.
     *
     * @param config the client config
     * @return the created client
     */
    @NonNull
    public static Pelican4J<PelicanPanelActions> forPelican(@NonNull ClientConfig config) {
        return new Pelican4J<>(config, PelicanPanelActions.class);
    }

    /**
     * Create a new API client
     * for the Pterodactyl panel.
     *
     * @param config the client config
     * @return the created client
     */
    @NonNull
    public static Pelican4J<PteroPanelActions> forPtero(@NonNull ClientConfig config) {
        return new Pelican4J<>(config, PteroPanelActions.class);
    }
}