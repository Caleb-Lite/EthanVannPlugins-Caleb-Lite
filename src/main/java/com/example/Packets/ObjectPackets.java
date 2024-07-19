package com.example.Packets;

import com.example.EthanApiPlugin.Collections.query.TileObjectQuery;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sending packets related to objects.
 */
public class ObjectPackets {

    /**
     * Queues an action packet for the specified object.
     *
     * @param actionFieldNo the action field number (1-5).
     * @param objectId      the ID of the object.
     * @param worldPointX   the X coordinate of the world point.
     * @param worldPointY   the Y coordinate of the world point.
     * @param ctrlDown      whether the control key is held down.
     */
    @SneakyThrows
    public static void queueObjectAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
                                         boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the appropriate packet based on the action field number
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getOpLoc1(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getOpLoc2(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getOpLoc3(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getOpLoc4(), objectId, worldPointX, worldPointY, ctrl);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getOpLoc5(), objectId, worldPointX, worldPointY, ctrl);
                break;
        }
    }

    /**
     * Queues an action packet for the specified object based on a list of actions.
     *
     * @param object     the object.
     * @param ctrlDown   whether the control key is held down.
     * @param actionlist the list of actions.
     */
    @SneakyThrows
    public static void queueObjectAction(TileObject object, boolean ctrlDown, String... actionlist) {
        if (object == null) {
            return;
        }
        // Get the object composition
        ObjectComposition comp = TileObjectQuery.getObjectComposition(object);
        if (comp == null) {
            return;
        }
        if (comp.getActions() == null) {
            return;
        }
        // Convert actions to a list and make them lowercase
        List<String> actions = Arrays.stream(comp.getActions()).collect(Collectors.toList());
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) == null)
                continue;
            actions.set(i, actions.get(i).toLowerCase());
        }
        // Determine the point based on the object type
        Point p;
        if (object instanceof GameObject) {
            GameObject gameObject = (GameObject) object;
            p = gameObject.getSceneMinLocation();
        } else {
            p = new Point(object.getLocalLocation().getSceneX(), object.getLocalLocation().getSceneY());
        }
        // Convert the point to local and world points
        LocalPoint lp = new LocalPoint(p.getX(), p.getY());
        WorldPoint wp = WorldPoint.fromScene(PacketReflection.getClient(), lp.getX(), lp.getY(), object.getPlane());
        // Find the action number based on the action list
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && action.equalsIgnoreCase(action2.toLowerCase())) {
                    num = actions.indexOf(action) + 1;
                }
            }
        }

        // Validate the action number
        if (num < 1 || num > 10) {
            return;
        }
        // Queue the object action
        queueObjectAction(num, object.getId(), wp.getX(), wp.getY(), ctrlDown);
    }

    /**
     * Queues a widget-on-object action packet.
     *
     * @param objectId      the ID of the object.
     * @param worldPointX   the X coordinate of the world point.
     * @param worldPointY   the Y coordinate of the world point.
     * @param sourceSlot    the source slot.
     * @param sourceItemId  the source item ID.
     * @param sourceWidgetId the source widget ID.
     * @param ctrlDown      whether the control key is held down.
     */
    public static void queueWidgetOnTileObject(int objectId, int worldPointX, int worldPointY, int sourceSlot,
                                               int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the widget-on-object packet
        PacketReflection.sendPacket(PacketDef.getOpLocT(), objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
                sourceWidgetId, ctrl);
    }

    /**
     * Queues a widget-on-object action packet.
     *
     * @param widget the widget.
     * @param object the object.
     */
    public static void queueWidgetOnTileObject(Widget widget, TileObject object) {
        // Determine the point based on the object type
        Point p;
        if (object instanceof GameObject) {
            GameObject gameObject = (GameObject) object;
            p = gameObject.getSceneMinLocation();
        } else {
            p = new Point(object.getLocalLocation().getSceneX(), object.getLocalLocation().getSceneY());
        }
        // Convert the point to local and world points
        LocalPoint lp = new LocalPoint(p.getX(), p.getY());
        WorldPoint wp = WorldPoint.fromScene(PacketReflection.getClient(), lp.getX(), lp.getY(), object.getPlane());
        // Queue the widget-on-object action
        queueWidgetOnTileObject(object.getId(), wp.getX(), wp.getY(), widget.getIndex(),
                widget.getItemId(),
                widget.getId(),
                false);
    }
}