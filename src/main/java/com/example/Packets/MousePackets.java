package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Executors;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

/**
 * Utility class for sending mouse click packets.
 */
public class MousePackets {

    // Get an instance of the Client class from RuneLite
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    // Random number generator
    private static final Random random = new Random();

    // Initial random delay
    private static long randomDelay = randomDelay();

    /**
     * Computes the modular inverse of a BigInteger value.
     *
     * @param val  the value to compute the modular inverse for.
     * @param bits the number of bits.
     * @return the modular inverse.
     */
    public static BigInteger modInverse(BigInteger val, int bits) {
        try {
            // Calculate 2^bits
            BigInteger shift = BigInteger.ONE.shiftLeft(bits);
            // Return the modular inverse of val mod 2^bits
            return val.modInverse(shift);
        } catch (ArithmeticException e) {
            // Return the original value if an exception occurs
            return val;
        }
    }

    /**
     * Computes the modular inverse of a long value.
     *
     * @param val the value to compute the modular inverse for.
     * @return the modular inverse.
     */
    public static long modInverse(long val) {
        // Convert long to BigInteger and compute the modular inverse
        return modInverse(BigInteger.valueOf(val), 64).longValue();
    }

    /**
     * Queues a mouse click packet at the specified coordinates.
     *
     * @param x the X coordinate of the click.
     * @param y the Y coordinate of the click.
     */
    @SneakyThrows
    public static void queueClickPacket(int x, int y) {
        // Get the current system time in milliseconds
        long mouseHandlerMS = System.currentTimeMillis();

        // Set the last mouse handler time to the current time
        setMouseHandlerLastMillis(mouseHandlerMS);

        // Get the last client time
        long clientMS = getClientLastMillis();

        // Calculate the difference between the current time and the last client time
        long deltaMs = mouseHandlerMS - clientMS;

        // Set the last client time to the current time
        setClientLastMillis(mouseHandlerMS);

        // Ensure deltaMs is within the range [0, 32767]
        if (deltaMs < 0) {
            deltaMs = 0L;
        }
        if (deltaMs > 32767) {
            deltaMs = 32767L;
        }

        // Shift deltaMs left by 1 bit
        int mouseInfo = ((int) deltaMs << 1);

        // Send a mouse click packet with the calculated mouseInfo and coordinates
        PacketReflection.sendPacket(PacketDef.getEventMouseClick(), mouseInfo, x, y);

        // Check if the client is idle and should log out
        if (checkIdleLogout()) {
            // Generate a new random delay
            randomDelay = randomDelay();

            // Submit a task to press a key to prevent idle logout
            Executors.newSingleThreadExecutor()
                    .submit(MousePackets::pressKey);
        }
    }

    /**
     * Queues a mouse click packet at the default coordinates (0, 0).
     */
    public static void queueClickPacket() {
        // Call the overloaded method with default coordinates
        queueClickPacket(0, 0);
    }

    /**
     * Checks if the client is idle and should log out.
     *
     * @return true if the client is idle and should log out, false otherwise.
     */
    private static boolean checkIdleLogout() {
        // Get the number of idle ticks for the keyboard
        int idleClientTicks = client.getKeyboardIdleTicks();

        // Use the smaller value between keyboard and mouse idle ticks
        if (client.getMouseIdleTicks() < idleClientTicks) {
            idleClientTicks = client.getMouseIdleTicks();
        }

        // Return true if idle ticks are greater than or equal to the random delay
        return idleClientTicks >= randomDelay;
    }

    /**
     * Generates a random delay for idle logout.
     *
     * @return the random delay.
     */
    private static long randomDelay() {
        // Generate a random delay based on a Gaussian distribution
        return (long) clamp(
                Math.round(random.nextGaussian() * 8000)
        );
    }

    /**
     * Clamps a value between 1 and 13000.
     *
     * @param val the value to clamp.
     * @return the clamped value.
     */
    private static double clamp(double val) {
        // Ensure the value is within the range [1, 13000]
        return Math.max(1, Math.min(13000, val));
    }

    /**
     * Simulates a key press event.
     */
    private static void pressKey() {
        // Create and dispatch a key press event
        KeyEvent keyPress = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), BUTTON1_DOWN_MASK, KeyEvent.VK_BACK_SPACE);
        client.getCanvas().dispatchEvent(keyPress);

        // Create and dispatch a key release event
        KeyEvent keyRelease = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);
        client.getCanvas().dispatchEvent(keyRelease);

        // Create and dispatch a key typed event
        KeyEvent keyTyped = new KeyEvent(client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED);
        client.getCanvas().dispatchEvent(keyTyped);
    }

    /**
     * Gets the last mouse handler milliseconds.
     *
     * @return the last mouse handler milliseconds.
     */
    @SneakyThrows
    public static long getMouseHandlerLastMillis() {
        // Load the mouse handler class
        Class<?> mouseHandler = client.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass);

        // Get the field for the last pressed time in milliseconds
        Field mouseHandlerLastPressedTime = mouseHandler.getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);

        // Make the field accessible
        mouseHandlerLastPressedTime.setAccessible(true);

        // Get the value of the field and multiply by the multiplier
        long retValue = mouseHandlerLastPressedTime.getLong(null) * Long.parseLong(ObfuscatedNames.mouseHandlerMillisMultiplier);

        // Make the field inaccessible again
        mouseHandlerLastPressedTime.setAccessible(false);

        // Return the retrieved value
        return retValue;
    }

    /**
     * Gets the last client milliseconds.
     *
     * @return the last client milliseconds.
     */
    @SneakyThrows
    public static long getClientLastMillis() {
        // Get the field for the last pressed time in milliseconds in the client class
        Field clientLastPressedTimeMillis = client.getClass().getDeclaredField(ObfuscatedNames.clientMillisField);

        // Make the field accessible
        clientLastPressedTimeMillis.setAccessible(true);

        // Get the value of the field and multiply by the multiplier
        long retValue = clientLastPressedTimeMillis.getLong(client) * Long.parseLong(ObfuscatedNames.clientMillisMultiplier);

        // Make the field inaccessible again
        clientLastPressedTimeMillis.setAccessible(false);

        // Return the retrieved value
        return retValue;
    }

    /**
     * Sets the last mouse handler milliseconds.
     *
     * @param time the time to set.
     */
    @SneakyThrows
    public static void setMouseHandlerLastMillis(long time) {
        // Load the mouse handler class
        Class<?> mouseHandler = client.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass);

        // Get the field for the last pressed time in milliseconds
        Field mouseHandlerLastPressedTime = mouseHandler.getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);

        // Make the field accessible
        mouseHandlerLastPressedTime.setAccessible(true);

        // Set the value of the field after multiplying by the modular inverse of the multiplier
        mouseHandlerLastPressedTime.setLong(null, time * modInverse(Long.parseLong(ObfuscatedNames.mouseHandlerMillisMultiplier)));

        // Make the field inaccessible again
        mouseHandlerLastPressedTime.setAccessible(false);
    }

    /**
     * Sets the last client milliseconds.
     *
     * @param time the time to set.
     */
    @SneakyThrows
    public static void setClientLastMillis(long time) {
        // Get the field for the last pressed time in milliseconds in the client class
        Field clientLastPressedTimeMillis = client.getClass().getDeclaredField(ObfuscatedNames.clientMillisField);

        // Make the field accessible
        clientLastPressedTimeMillis.setAccessible(true);

        // Set the value of the field after multiplying by the modular inverse of the multiplier
        clientLastPressedTimeMillis.setLong(client, time * modInverse(Long.parseLong(ObfuscatedNames.clientMillisMultiplier)));

        // Make the field inaccessible again
        clientLastPressedTimeMillis.setAccessible(false);
    }
}