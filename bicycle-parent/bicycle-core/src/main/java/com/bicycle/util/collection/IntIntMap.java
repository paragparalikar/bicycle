package com.bicycle.util.collection;

public class IntIntMap extends HashBase {

    /**
     * The value indicating that the entry has not been found.
     */
    public static final int NOT_FOUND = -1;

    private static final int DELETED = 1;
    private int[] keys;
    private int[] values;
    private int zeroValue;

    @Override
    protected void reset(int newLevel) {
        super.reset(newLevel);
        keys = new int[len];
        values = new int[len];
    }

    /**
     * Store the given key-value pair. The value is overwritten or added.
     *
     * @param key the key
     * @param value the value (-1 is not supported)
     */
    public void put(int key, int value) {
        if (key == 0) {
            zeroKey = true;
            zeroValue = value;
            return;
        }
        checkSizePut();
        internalPut(key, value);
    }

    private void internalPut(int key, int value) {
        int index = getIndex(key);
        int plus = 1;
        int deleted = -1;
        do {
            int k = keys[index];
            if (k == 0) {
                if (values[index] != DELETED) {
                    // found an empty record
                    if (deleted >= 0) {
                        index = deleted;
                        deletedCount--;
                    }
                    size++;
                    keys[index] = key;
                    values[index] = value;
                    return;
                }
                // found a deleted record
                if (deleted < 0) {
                    deleted = index;
                }
            } else if (k == key) {
                // update existing
                values[index] = value;
                return;
            }
            index = (index + plus++) & mask;
        } while (plus <= len);
        // no space
        throw new RuntimeException("hashmap is full");
    }

    /**
     * Remove the key-value pair with the given key.
     *
     * @param key the key
     */
    public void remove(int key) {
        if (key == 0) {
            zeroKey = false;
            return;
        }
        checkSizeRemove();
        int index = getIndex(key);
        int plus = 1;
        do {
            int k = keys[index];
            if (k == key) {
                // found the record
                keys[index] = 0;
                values[index] = DELETED;
                deletedCount++;
                size--;
                return;
            } else if (k == 0 && values[index] == 0) {
                // found an empty record
                return;
            }
            index = (index + plus++) & mask;
        } while (plus <= len);
        // not found
    }

    @Override
    protected void rehash(int newLevel) {
        int[] oldKeys = keys;
        int[] oldValues = values;
        reset(newLevel);
        for (int i = 0; i < oldKeys.length; i++) {
            int k = oldKeys[i];
            if (k != 0) {
                // skip the checkSizePut so we don't end up
                // accidentally recursing
                internalPut(k, oldValues[i]);
            }
        }
    }

    /**
     * Get the value for the given key. This method returns NOT_FOUND if the
     * entry has not been found.
     *
     * @param key the key
     * @return the value or NOT_FOUND
     */
    public int get(int key) {
        if (key == 0) {
            return zeroKey ? zeroValue : NOT_FOUND;
        }
        int index = getIndex(key);
        int plus = 1;
        do {
            int k = keys[index];
            if (k == 0 && values[index] == 0) {
                // found an empty record
                return NOT_FOUND;
            } else if (k == key) {
                // found it
                return values[index];
            }
            index = (index + plus++) & mask;
        } while (plus <= len);
        return NOT_FOUND;
    }

}
