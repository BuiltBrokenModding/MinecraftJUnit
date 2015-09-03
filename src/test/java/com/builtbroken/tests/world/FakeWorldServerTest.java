package com.builtbroken.tests.world;

import com.builtbroken.mc.testing.junit.world.FakeWorldServer;

/**
 * JUnit test for {@link FakeWorldServer}
 * Created by Dark on 9/3/2015.
 */
public class FakeWorldServerTest extends FakeWorldTest
{
    @Override
    public void setUpForEntireClass()
    {
        world = FakeWorldServer.newWorld("FakeWorldServerTest");
    }
}
