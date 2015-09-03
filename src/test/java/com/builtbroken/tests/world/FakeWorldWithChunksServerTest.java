package com.builtbroken.tests.world;

import com.builtbroken.mc.testing.junit.world.FakeWorldWithChunksServer;

/**
 * JUnit test for {@link FakeWorldWithChunksServer}
 * Created by Dark on 9/3/2015.
 */
public class FakeWorldWithChunksServerTest extends FakeWorldTest
{
    @Override
    public void setUpForEntireClass()
    {
        world = FakeWorldWithChunksServer.newWorld("FakeWorldWithChunksServerTest");
    }
}
