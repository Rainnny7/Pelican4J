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
package me.braydon.pelican.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

/**
 * A server node model.
 *
 * @author Braydon
 * @see <a href="https://dashflo.net/docs/api/pterodactyl/v1/#req_9c01cbbf259f4b1f8aa6ff3b51dadd0e">Model Scheme</a>
 */
@AllArgsConstructor @Getter @ToString
public final class Node extends PanelModel<Node> {
    /**
     * The numericId of this node.
     */
    private final int id;

    /**
     * The UUID of this node.
     */
    @NonNull private final UUID uuid;

    /**
     * The name of this node.
     */
    @NonNull private final String name;

    /**
     * The description of this node, null if none.
     */
    private final String description;

    /**
     * The ID of the location of this node.
     */
    @SerializedName("location_id") private final int locationId;

    /**
     * The FQDN scheme of this node.
     */
    @NonNull private final String scheme;

    /**
     * The FQDN of this node.
     */
    @NonNull private final String fqdn;

    /**
     * The maximum size of a file that can be uploaded to this node.
     */
    @SerializedName("upload_size") private final long maxUploadSize;

    /**
     * The memory allocated to this node.
     */
    private final long memory;

    /**
     * The memory overallocation of this node.
     */
    @SerializedName("memory_overallocate") private final long memoryOverallocation;

    /**
     * The disk allocated to this node.
     */
    private final long disk;

    /**
     * The disk overallocation of this node.
     */
    @SerializedName("disk_overallocate") private final long diskOverallocation;

    /**
     * The port this node listens on.
     */
    @SerializedName("daemon_listen") private final int port;

    /**
     * The SFTP port this node listens on.
     */
    @SerializedName("daemon_sftp") private final int sftpPort;

    /**
     * The volumes directory of this node.
     * <p>
     * This is where the server files are stored.
     * </p>
     */
    @SerializedName("daemon_base") @NonNull private final String volumesDirectory;

    /**
     * Whether this node is public.
     */
    @SerializedName("public") private final boolean publiclyVisible;

    /**
     * Whether this node is behind a proxy.
     */
    @SerializedName("behind_proxy") private final boolean behindProxy;

    /**
     * Whether this node is in maintenance mode.
     */
    @SerializedName("maintenance_mode") private final boolean maintenanceMode;

    /**
     * The date this node was last updated.
     */
    @SerializedName("updated_at") @NonNull private final Date lastUpdated;

    /**
     * The date this node was created.
     */
    @SerializedName("created_at") @NonNull private final Date created;
}