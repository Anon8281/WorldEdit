/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.extension.input;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.extension.registry.MaskRegistry;
import com.sk89q.worldedit.extent.Extent;

import javax.annotation.Nullable;

/**
 * Contains contextual information that may be useful when constructing
 * objects from a registry (such as {@link MaskRegistry}).
 * </p>
 * By default, {@link #isRestricted()} will return true.
 */
public class ParserContext {

    private @Nullable Extent extent;
    private @Nullable LocalSession session;
    private @Nullable LocalWorld world;
    private @Nullable LocalPlayer player;
    private boolean restricted = true;
    private boolean preferringWildcard;

    /**
     * Get the {@link Extent} set on this context.
     *
     * @return an extent
     */
    public @Nullable Extent getExtent() {
        return extent;
    }

    /**
     * Set the extent.
     *
     * @param extent an extent, or null if none is available
     */
    public void setExtent(@Nullable Extent extent) {
        this.extent = extent;
    }

    /**
     * Get the {@link LocalSession}.
     *
     * @return a session
     */
    public @Nullable LocalSession getSession() {
        return session;
    }

    /**
     * Set the session.
     *
     * @param session a session, or null if none is available
     */
    public void setSession(@Nullable LocalSession session) {
        this.session = session;
    }

    /**
     * Get the {@link LocalWorld} set on this context.
     *
     * @return a world
     */
    public @Nullable LocalWorld getWorld() {
        return world;
    }

    /**
     * Set the world.
     *
     * @param world a world, or null if none is available
     */
    public void setWorld(@Nullable LocalWorld world) {
        this.world = world;
    }

    /**
     * Get the {@link LocalPlayer} set on this context.
     *
     * @return a player
     */
    public @Nullable LocalPlayer getPlayer() {
        return player;
    }

    /**
     * Set the player.
     *
     * @param player a player, or null if none is available
     */
    public void setPlayer(@Nullable LocalPlayer player) {
        this.player = player;
    }

    /**
     * Get the {@link Extent} set on this context.
     *
     * @return an extent
     * @throws InputParseException thrown if no {@link Extent} is set
     */
    public Extent requireExtent() throws InputParseException {
        Extent extent = getExtent();
        if (extent == null) {
            throw new InputParseException("No Extent is known");
        }
        return extent;
    }

    /**
     * Get the {@link LocalSession}.
     *
     * @return a session
     * @throws InputParseException thrown if no {@link LocalSession} is set
     */
    public LocalSession requireSession() throws InputParseException {
        LocalSession session = getSession();
        if (session == null) {
            throw new InputParseException("No LocalSession is known");
        }
        return session;
    }

    /**
     * Get the {@link LocalWorld} set on this context.
     *
     * @return a world
     * @throws InputParseException thrown if no {@link LocalWorld} is set
     */
    public LocalWorld requireWorld() throws InputParseException {
        LocalWorld world = getWorld();
        if (world == null) {
            throw new InputParseException("No world is known");
        }
        return world;
    }

    /**
     * Get the {@link LocalPlayer} set on this context.
     *
     * @return a player
     * @throws InputParseException thrown if no {@link LocalPlayer} is set
     */
    public LocalPlayer requirePlayer() throws InputParseException {
        LocalPlayer player = getPlayer();
        if (player == null) {
            throw new InputParseException("No player is known");
        }
        return player;
    }

    /**
     * Returns whether there should be restrictions (as a result of
     * limits or permissions) considered when parsing the input.
     *
     * @return true if restricted
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * Set whether there should be restrictions (as a result of
     * limits or permissions) considered when parsing the input.
     *
     * @param restricted true if restricted
     */
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    /**
     * Get whether wildcards are preferred.
     *
     * @return true if wildcards are preferred
     */
    public boolean isPreferringWildcard() {
        return preferringWildcard;
    }

    /**
     * Set whether wildcards are preferred.
     *
     * @param preferringWildcard true if wildcards are preferred
     */
    public void setPreferringWildcard(boolean preferringWildcard) {
        this.preferringWildcard = preferringWildcard;
    }

}