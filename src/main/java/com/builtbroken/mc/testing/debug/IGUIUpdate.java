package com.builtbroken.mc.testing.debug;

/**
 * Applies to objects that can receive update tick calls.
 */
public interface IGUIUpdate
{
    /**
     * Called to update the object when registered to a tick handler
     * @return true if the object should keep updating, if false it will
     * be removed from the tick handler. In which it is up to you to re-add
     * it whe it needs more updates.
     */
    boolean update();
}
