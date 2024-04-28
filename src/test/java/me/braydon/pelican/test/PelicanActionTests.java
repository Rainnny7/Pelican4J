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
package me.braydon.pelican.test;

import me.braydon.pelican.action.pelican.PelicanPanelActions;
import me.braydon.pelican.client.ClientConfig;
import me.braydon.pelican.client.Pelican4J;
import me.braydon.pelican.model.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for executing actions
 * on the Pterodactyl panel.
 *
 * @author Braydon
 */
public final class PelicanActionTests {
    /**
     * The client for the tests.
     */
    private static Pelican4J<PelicanPanelActions> client;

    /**
     * Setup the client for the tests.
     */
    @BeforeAll
    static void setup() {
        client = Pelican4J.forPelican(ClientConfig.builder()
                .panelUrl(System.getenv("TEST_PANEL_URL"))
                .apiKey(System.getenv("TEST_APPS_API_KEY"))
                .build()); // Create a new Pelican client
    }

    /**
     * Tests for the application actions.
     */
    @Nested
    class Application {
        /**
         * Test getting a list of
         * nodes from the panel.
         */
        @Test
        void testGetNodes() {
            Node node = client.actions().application().nodes().getDetails(1).execute();
            System.out.println("node = " + node);
        }
    }
}