package com.example.Packets;

import com.example.EthanApiPlugin.Collections.ETileItem;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.widgets.Widget;

/**
 * Utility class for sending packets related to tile items.
 */
public class TileItemPackets {

    /**
     * Queues an action packet for the specified tile item.
     *
     * @param actionFieldNo the action field number (1-5).
     * @param objectId      the ID of the tile item.
     * @param worldPointX   the X coordinate of the world point.
     * @param worldPointY   the Y coordinate of the world point.
     * @param ctrlDown      whether the control key is held down.
     */
    @SneakyThrows
    public static void queueTileItemAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
                                           boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the appropriate packet based on the action field number
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getOpObj1(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getOpObj2(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getOpObj3(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getOpObj4(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getOpObj5(), objectId, worldPointX, worldPointY, ctrl);
                break;
        }
    }

    /**
     * Queues a widget-on-tile-item action packet.
     *
     * @param objectId      the ID of the tile item.
     * @param worldPointX   the X coordinate of the world point.
     * @param worldPointY   the Y coordinate of the world point.
     * @param sourceSlot    the source slot.
     * @param sourceItemId  the source item ID.
     * @param sourceWidgetId the source widget ID.
     * @param ctrlDown      whether the control key is held down.
     */
    public static void queueWidgetOnTileItem(int objectId, int worldPointX, int worldPointY, int sourceSlot,
                                             int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the widget-on-tile-item packet
        PacketReflection.sendPacket(PacketDef.getOpObjT(), objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
                sourceWidgetId, ctrl);
    }

    /**
     * Queues an action packet for the specified tile item.
     *
     * @param item     the tile item.
     * @param ctrlDown whether the control key is held down.
     */
    public static void queueTileItemAction(ETileItem item, boolean ctrlDown) {
        // Queue the tile item action with action field number 3
        queueTileItemAction(3, item.getTileItem().getId(), item.getLocation().getX(), item.getLocation().getY(),
                ctrlDown);
    }

    /**
     * Queues a widget-on-tile-item action packet.
     *
     * @param item     the tile item.
     * @param widget   the widget.
     * @param ctrlDown whether the control key is held down.
     */
    public static void queueWidgetOnTileItem(ETileItem item, Widget widget, boolean ctrlDown) {
        // Queue the widget-on-tile-item action
        queueWidgetOnTileItem(item.getTileItem().getId(), item.getLocation().getX(), item.getLocation().getY(),
                widget.getIndex(), widget.getItemId(), widget.getId(), ctrlDown);
    }
}