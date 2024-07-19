package com.example.Packets;

import com.example.EthanApiPlugin.Collections.query.NPCQuery;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sending packets related to NPCs.
 */
public class NPCPackets {

    /**
     * Queues an action packet for the specified NPC.
     *
     * @param actionFieldNo the action field number (1-5).
     * @param npcIndex      the index of the NPC.
     * @param ctrlDown      whether the control key is held down.
     */
    @SneakyThrows
    public static void queueNPCAction(int actionFieldNo, int npcIndex, boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the appropriate packet based on the action field number
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getOpNpc1(), npcIndex, ctrl);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getOpNpc2(), npcIndex, ctrl);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getOpNpc3(), npcIndex, ctrl);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getOpNpc4(), npcIndex, ctrl);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getOpNpc5(), npcIndex, ctrl);
                break;
        }
    }

    /**
     * Queues an action packet for the specified NPC based on a list of actions.
     *
     * @param npc        the NPC.
     * @param actionList the list of actions.
     */
    @SneakyThrows
    public static void queueNPCAction(NPC npc, String... actionList) {
        if (npc == null) {
            return;
        }
        // Get the NPC composition
        NPCComposition comp = NPCQuery.getNPCComposition(npc);
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
        // Find the action number based on the action list
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionList) {
                if (action != null && action.equalsIgnoreCase(action2)) {
                    num = actions.indexOf(action.toLowerCase()) + 1;
                }
            }
        }

        // Validate the action number
        if (num < 1 || num > 10) {
            return;
        }
        // Queue the NPC action
        queueNPCAction(num, npc.getIndex(), false);
    }

    /**
     * Queues a widget-on-NPC action packet.
     *
     * @param npcIndex      the index of the NPC.
     * @param sourceItemId  the source item ID.
     * @param sourceSlot    the source slot.
     * @param sourceWidgetId the source widget ID.
     * @param ctrlDown      whether the control key is held down.
     */
    public static void queueWidgetOnNPC(int npcIndex, int sourceItemId, int sourceSlot, int sourceWidgetId,
                                        boolean ctrlDown) {
        // Determine the control flag based on whether the control key is held down
        int ctrl = ctrlDown ? 1 : 0;
        // Send the widget-on-NPC packet
        PacketReflection.sendPacket(PacketDef.getOpNpcT(), npcIndex, sourceItemId, sourceSlot, sourceWidgetId, ctrl);
    }

    /**
     * Queues a widget-on-NPC action packet.
     *
     * @param npc    the NPC.
     * @param widget the widget.
     */
    public static void queueWidgetOnNPC(NPC npc, Widget widget) {
        // Call the overloaded method with the NPC index and widget details
        queueWidgetOnNPC(npc.getIndex(), widget.getItemId(), widget.getIndex(), widget.getId(), false);
    }
}