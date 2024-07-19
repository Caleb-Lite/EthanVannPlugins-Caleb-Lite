package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;

import java.lang.reflect.Field;

/**
 * Utility class for manipulating buffer fields using reflection.
 */
public class BufferMethods {

    /**
     * Sets the offset of the buffer instance.
     *
     * @param bufferInstance the buffer instance.
     * @param offset         the offset to set.
     */
    public static void setOffset(Object bufferInstance, int offset) {
        try {
            // Get the offset field from the buffer instance class
            Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
            // Make the field accessible
            offsetField.setAccessible(true);
            // Set the offset value
            offsetField.setInt(bufferInstance, offset);
            // Make the field inaccessible again
            offsetField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }

    /**
     * Gets the offset of the buffer instance.
     *
     * @param bufferInstance the buffer instance.
     * @return the offset value.
     */
    public static int getOffset(Object bufferInstance) {
        try {
            // Get the offset field from the buffer instance class
            Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
            // Make the field accessible
            offsetField.setAccessible(true);
            // Get the offset value
            int offset = offsetField.getInt(bufferInstance);
            // Make the field inaccessible again
            offsetField.setAccessible(false);
            // Return the offset value
            return offset;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
        // Return -1 if an exception occurs
        return -1;
    }

    /**
     * Sets the byte array of the buffer instance.
     *
     * @param bufferInstance the buffer instance.
     * @param array          the byte array to set.
     */
    public static void setArray(Object bufferInstance, byte[] array) {
        try {
            // Get the array field from the buffer instance class
            Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
            // Make the field accessible
            arrayField.setAccessible(true);
            // Set the byte array
            arrayField.set(bufferInstance, array);
            // Make the field inaccessible again
            arrayField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }

    /**
     * Gets the byte array of the buffer instance.
     *
     * @param bufferInstance the buffer instance.
     * @return the byte array.
     */
    public static byte[] getArray(Object bufferInstance) {
        try {
            // Get the array field from the buffer instance class
            Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
            // Make the field accessible
            arrayField.setAccessible(true);
            // Get the byte array
            byte[] array = (byte[]) arrayField.get(bufferInstance);
            // Make the field inaccessible again
            arrayField.setAccessible(false);
            // Return the byte array
            return array;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
        // Return null if an exception occurs
        return null;
    }

    /**
     * Writes a value to the buffer instance based on the write description.
     *
     * @param writeDescription the description of how to write the value.
     * @param value            the value to write.
     * @param bufferInstance   the buffer instance.
     */
    public static void writeValue(String writeDescription, int value, Object bufferInstance) {
        // Determine the write type magnitude based on the write description
        int writeTypeMagnitude = writeDescription.contains("v") ? 0 : Integer.parseInt(writeDescription.substring(1).trim());
        // Get the byte array from the buffer instance
        byte[] arr = getArray(bufferInstance);
        // Calculate the next index based on the current offset
        int index = nextIndex(getOffset(bufferInstance));
        // Set the new offset in the buffer instance
        setOffset(bufferInstance, index);
        // Adjust the index based on the multiplier
        index = index * Integer.parseInt(ObfuscatedNames.indexMultiplier) - 1;
        // Write the value to the byte array based on the write description
        switch (writeDescription.charAt(0)) {
            case 's':
                setArray(bufferInstance, writeSub(writeTypeMagnitude, value, arr, index));
                break;
            case 'a':
                setArray(bufferInstance, writeAdd(writeTypeMagnitude, value, arr, index));
                break;
            case 'r':
                setArray(bufferInstance, writeRightShifted(writeTypeMagnitude, value, arr, index));
                break;
            case 'v':
                setArray(bufferInstance, writeVar(value, arr, index));
                break;
        }
    }

    /**
     * Writes a subtracted value to the byte array.
     *
     * @param subValue the value to subtract.
     * @param value    the value to write.
     * @param arr      the byte array.
     * @param index    the index to write to.
     * @return the modified byte array.
     */
    static byte[] writeSub(int subValue, int value, byte[] arr, int index) {
        // Subtract the value and store it in the array
        arr[index] = (byte) (subValue - value);
        return arr;
    }

    /**
     * Writes an added value to the byte array.
     *
     * @param addValue the value to add.
     * @param value    the value to write.
     * @param arr      the byte array.
     * @param index    the index to write to.
     * @return the modified byte array.
     */
    static byte[] writeAdd(int addValue, int value, byte[] arr, int index) {
        // Add the value and store it in the array
        arr[index] = (byte) (addValue + value);
        return arr;
    }

    /**
     * Writes a right-shifted value to the byte array.
     *
     * @param shiftAmount the amount to shift.
     * @param value       the value to write.
     * @param arr         the byte array.
     * @param index       the index to write to.
     * @return the modified byte array.
     */
    static byte[] writeRightShifted(int shiftAmount, int value, byte[] arr, int index) {
        // Right shift the value and store it in the array
        arr[index] = (byte) (value >> shiftAmount);
        return arr;
    }

    /**
     * Writes a value to the byte array.
     *
     * @param value the value to write.
     * @param arr   the byte array.
     * @param index the index to write to.
     * @return the modified byte array.
     */
    static byte[] writeVar(int value, byte[] arr, int index) {
        // Store the value in the array
        arr[index] = (byte) (value);
        return arr;
    }

    /**
     * Calculates the next index based on the current offset.
     *
     * @param offset the current offset.
     * @return the next index.
     */
    static public int nextIndex(int offset) {
        // Increment the offset based on the multiplier
        offset += (int) Long.parseLong(ObfuscatedNames.offsetMultiplier);
        return offset;
    }
}