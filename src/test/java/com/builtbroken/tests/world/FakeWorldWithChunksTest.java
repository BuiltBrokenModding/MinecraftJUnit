package com.builtbroken.tests.world;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorldWithChunks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by robert on 11/13/2014.
 */
@RunWith(VoltzTestRunner.class)
public class FakeWorldWithChunksTest extends FakeWorldTest
{
    @Override
    public void setUpForEntireClass()
    {
        world = FakeWorldWithChunks.newWorld("FakeWorldWithChunksTest");
        ((FakeWorldWithChunks) world).debugInfo = true;
    }

}
