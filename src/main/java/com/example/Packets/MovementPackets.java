package com.example.Packets;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import net.runelite.api.coords.WorldPoint;

/**
 * Utility class for sending movement-related packets.
 */
public class MovementPackets {

    /**
     * Queues a movement packet to the specified coordinates.
     *
     * @param worldPointX the X coordinate in the world.
     * @param worldPointY the Y coordinate in the world.
     * @param ctrlDown    whether the control key is held down.
     */
    public static void queueMovement(int worldPointX, int worldPointY, boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 2 : 0;

        // Send a movement packet with the specified coordinates and control flag
        PacketReflection.sendPacket(PacketDef.getMoveGameClick(), worldPointX, worldPointY, ctrl, 5);
    }

    /**
     * Queues a movement packet to the specified location.
     *
     * @param location the WorldPoint location to move to.
     */
    public static void queueMovement(WorldPoint location) {
        // Call the overloaded method with the coordinates from the WorldPoint object
        queueMovement(location.getX(), location.getY(), false);
    }
}