package com.builtbroken.tests.world;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.init.Bootstrap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

/**
 * JUnit test for {@link FakeWorldServer}
 * Created by Dark on 9/3/2015.
 */
public class TestFakeWorldServer extends TestFakeWorld
{
    static FakeDedicatedServer server;
    static FakeWorldServer world;

    @BeforeAll
    public static void setUpForEntireClass()
    {
        Bootstrap.register();
        FakeDedicatedServer.exceptionHandler = Assertions::fail;

        server = FakeWorldServer.createServer("FakeWorldServerTest");
        world = FakeWorldServer.newWorld(server, 0, "FakeWorldServerTest");
    }

    @AfterAll
    public static void afterAllTests()
    {
        server.dispose();
    }
}
