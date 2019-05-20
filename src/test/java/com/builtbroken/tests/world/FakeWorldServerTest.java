package com.builtbroken.tests.world;

import com.builtbroken.mc.testing.junit.InitLauncher;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.init.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * JUnit test for {@link FakeWorldServer}
 * Created by Dark on 9/3/2015.
 */
public class FakeWorldServerTest extends FakeWorldTest
{
    @BeforeAll
    public static void setUpForEntireClass()
    {
        Bootstrap.register();
        world = FakeWorldServer.newWorld("FakeWorldServerTest");
    }
}
