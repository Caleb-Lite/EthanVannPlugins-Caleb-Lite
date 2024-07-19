package com.example.Packets;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Player;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sending packets related to players.
 */
public class PlayerPackets {

    /**
     * Queues an action packet for the specified player.
     *
     * @param actionFieldNo the action field number (1-8).
     * @param playerIndex   the index of the player.
     * @param ctrlDown      whether the control key is held down.
     */
    @SneakyThrows
    public static void queuePlayerAction(int actionFieldNo, int playerIndex, boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the appropriate packet based on the action field number
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getOpPlayer1(), playerIndex, ctrl);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getOpPlayer2(), playerIndex, ctrl);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getOpPlayer3(), playerIndex, ctrl);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getOpPlayer4(), playerIndex, ctrl);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getOpPlayer5(), playerIndex, ctrl);
                break;
            case 6:
                PacketReflection.sendPacket(PacketDef.getOpPlayer6(), playerIndex, ctrl);
                break;
            case 7:
                PacketReflection.sendPacket(PacketDef.getOpPlayer7(), playerIndex, ctrl);
                break;
            case 8:
                PacketReflection.sendPacket(PacketDef.getOpPlayer8(), playerIndex, ctrl);
                break;
        }
    }

    /**
     * Queues an action packet for the specified player based on a list of actions.
     *
     * @param player     the player.
     * @param actionlist the list of actions.
     */
    @SneakyThrows
    public static void queuePlayerAction(Player player, String... actionlist) {
        // Get the list of player actions from the client
        List<String> actions = Arrays.stream(PacketReflection.getClient().getPlayerOptions()).collect(Collectors.toList());
        // Convert actions to lowercase
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) == null)
                continue;
            actions.set(i, actions.get(i).toLowerCase());
        }
        // Find the action number based on the action list
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && action.equalsIgnoreCase(action2)) {
                    num = actions.indexOf(action.toLowerCase()) + 1;
                }
            }
        }

        // Validate the action number
        if (num < 1 || num > 10) {
            return;
        }
        // Queue the player action
        queuePlayerAction(num, player.getId(), false);
    }

    /**
     * Queues a widget-on-player action packet.
     *
     * @param playerIndex    the index of the player.
     * @param sourceItemId   the source item ID.
     * @param sourceSlot     the source slot.
     * @param sourceWidgetId the source widget ID.
     * @param ctrlDown       whether the control key is held down.
     */
    public static void queueWidgetOnPlayer(int playerIndex, int sourceItemId, int sourceSlot, int sourceWidgetId,
                                           boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the widget-on-player packet
        PacketReflection.sendPacket(PacketDef.getOpPlayerT(), playerIndex, sourceItemId, sourceSlot, sourceWidgetId, ctrl);
    }

    /**
     * Queues a widget-on-player action packet.
     *
     * @param player  the player.
     * @param widget  the widget.
     */
    public static void queueWidgetOnPlayer(Player player, Widget widget) {
        // Call the overloaded method with the player index and widget details
        queueWidgetOnPlayer(player.getId(), widget.getItemId(), widget.getIndex(), widget.getId(), false);
    }
}