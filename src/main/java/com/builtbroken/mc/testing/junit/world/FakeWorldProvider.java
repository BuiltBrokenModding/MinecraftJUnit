package com.builtbroken.mc.testing.junit.world;

import net.minecraft.world.WorldProvider;

/** World provider used by the fake world during unit testing.
 * Is only used to mock data that the world needs from the
 * world provider during creation
 *
 * Created by robert on 11/20/2014.
 */
public class FakeWorldProvider extends WorldProvider
{
    @Override
    public String getDimensionName()
    {
        return "FakeWorld";
    }
}
