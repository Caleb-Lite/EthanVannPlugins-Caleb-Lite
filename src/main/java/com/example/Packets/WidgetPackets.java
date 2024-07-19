package com.example.Packets;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sending packets related to widgets.
 */
public class WidgetPackets {

    /**
     * Queues an action packet for the specified widget.
     *
     * @param actionFieldNo the action field number (1-10).
     * @param widgetId      the ID of the widget.
     * @param itemId        the item ID associated with the widget.
     * @param childId       the child ID of the widget.
     */
    @SneakyThrows
    public static void queueWidgetActionPacket(int actionFieldNo, int widgetId, int itemId, int childId) {
        // Send the appropriate packet based on the action field number
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getIfButton1(), widgetId, childId, itemId);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getIfButton2(), widgetId, childId, itemId);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getIfButton3(), widgetId, childId, itemId);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getIfButton4(), widgetId, childId, itemId);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getIfButton5(), widgetId, childId, itemId);
                break;
            case 6:
                PacketReflection.sendPacket(PacketDef.getIfButton6(), widgetId, childId, itemId);
                break;
            case 7:
                PacketReflection.sendPacket(PacketDef.getIfButton7(), widgetId, childId, itemId);
                break;
            case 8:
                PacketReflection.sendPacket(PacketDef.getIfButton8(), widgetId, childId, itemId);
                break;
            case 9:
                PacketReflection.sendPacket(PacketDef.getIfButton9(), widgetId, childId, itemId);
                break;
            case 10:
                PacketReflection.sendPacket(PacketDef.getIfButton10(), widgetId, childId, itemId);
                break;
        }
    }

    /**
     * Queues an action packet for the specified widget based on a list of actions.
     *
     * @param widget     the widget.
     * @param actionlist the list of actions.
     */
    @SneakyThrows
    public static void queueWidgetAction(Widget widget, String... actionlist) {
        // Check if the widget is null
        if (widget == null) {
            System.out.println("call to queueWidgetAction with null widget");
            return;
        }
        // Get the list of widget actions and convert them to lowercase
        List<String> actions = Arrays.stream(widget.getActions()).collect(Collectors.toList());
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) == null)
                continue;
            actions.set(i, actions.get(i).toLowerCase());
        }
        // Find the action number based on the action list
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && Text.removeTags(action).equalsIgnoreCase(action2)) {
                    num = actions.indexOf(action.toLowerCase()) + 1;
                }
            }
        }

        // Validate the action number
        if (num < 1 || num > 10) {
            return;
        }
        // Queue the widget action packet
        queueWidgetActionPacket(num, widget.getId(), widget.getItemId(), widget.getIndex());
    }

    /**
     * Queues a widget-on-widget action packet.
     *
     * @param srcWidget  the source widget.
     * @param destWidget the destination widget.
     */
    public static void queueWidgetOnWidget(Widget srcWidget, Widget destWidget) {
        // Call the overloaded method with the widget details
        queueWidgetOnWidget(srcWidget.getId(), srcWidget.getIndex(), srcWidget.getItemId(), destWidget.getId(), destWidget.getIndex(), destWidget.getItemId());
    }

    /**
     * Queues a widget-on-widget action packet.
     *
     * @param sourceWidgetId      the ID of the source widget.
     * @param sourceSlot          the source slot.
     * @param sourceItemId        the source item ID.
     * @param destinationWidgetId the ID of the destination widget.
     * @param destinationSlot     the destination slot.
     * @param destinationItemId   the destination item ID.
     */
    public static void queueWidgetOnWidget(int sourceWidgetId, int sourceSlot, int sourceItemId,
                                           int destinationWidgetId, int destinationSlot, int destinationItemId) {
        // Send the widget-on-widget packet
        PacketReflection.sendPacket(PacketDef.getIfButtonT(), sourceWidgetId, sourceSlot, sourceItemId, destinationWidgetId,
                destinationSlot, destinationItemId);
    }

    /**
     * Queues a resume pause action packet.
     *
     * @param widgetId the ID of the widget.
     * @param childId  the child ID of the widget.
     */
    public static void queueResumePause(int widgetId, int childId) {
        // Send the resume pause packet
        PacketReflection.sendPacket(PacketDef.getResumePausebutton(), widgetId, childId);
    }
}