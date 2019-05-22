package com.builtbroken.mc.testing.junit.prefabs;

import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.init.Bootstrap;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-22.
 */
public class WorldTest extends AbstractTest
{
    protected static World world = null;

    @BeforeAll
    public static void setUpForEntireClass()
    {
        Bootstrap.register();
        world = FakeWorld.newWorld("FakeWorldTest");
    }
}
