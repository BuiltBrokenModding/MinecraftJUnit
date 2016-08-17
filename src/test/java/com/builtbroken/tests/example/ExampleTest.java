package com.builtbroken.tests.example;

import net.minecraft.world.World;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import org.junit.runner.RunWith;

/**
 * @author Tyler
 */
@RunWith(VoltzTestRunner.class)
public class ExampleTest extends AbstractTest {
    World world;

    @Override
    public void setUpForEntireClass() {
        world = FakeWorld.newWorld("NewTestWorld");
    }
}
