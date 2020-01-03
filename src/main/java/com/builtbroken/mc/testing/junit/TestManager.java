package com.builtbroken.mc.testing.junit;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.util.function.Consumer;

/**
 * Created by Dark(DarkGuardsman, Robert) on 12/31/2019.
 */
public class TestManager
{
    private FakeWorldServer world;
    private TestPlayer player;
    private FakeDedicatedServer server;

    private final String name;
    private final Consumer<String> errorHandler;

    public TestManager(String name, Consumer<String> errorHandler)
    {
        this.name = name;
        this.errorHandler = errorHandler;
    }

    public FakeDedicatedServer getServer()
    {
        if (server == null)
        {
            Bootstrap.register();
            server = FakeWorldServer.createServer(name);
            server.exceptionHandler = errorHandler;
        }
        return server;
    }

    public FakeWorldServer getWorld()
    {
        if (world == null)
        {
            world = FakeWorldServer.newWorld(getServer(), 0, name);
            world.init();
        }
        return world;
    }

    public TestPlayer getPlayer()
    {
        if (player == null)
        {
            player = new TestPlayer(getServer(), getWorld());
        }
        return player;
    }

    /**
     * Destroys everything at the end of the test
     */
    public void tearDownTest()
    {
        getServer().dispose();
    }

    /**
     * Cleans up data between tests
     */
    public void cleanupBetweenTests()
    {
        if (player != null)
        {
            player.reset();
        }

        if (world != null)
        {
            DimensionManager.setWorld(0, null, getServer());
            world = null;
        }
    }

    public void clearCenterChunk()
    {
        //Clear chunk to make sure each test is valid
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y++)
                {
                    pos.setPos(x, y, z);
                    getWorld().setBlockToAir(pos);
                }
            }
        }
    }

    public void lockToCenterChunk(Consumer<String> fail) {
        //Limits block placements to inside our clean area so we can ensure each test case is valid
        getWorld().setBlockCallback = (pos, state) ->
        {
            if (pos.getX() < 0 || pos.getX() > 15 || pos.getZ() < 0 || pos.getZ() > 15)
            {
                fail.accept("Placed block outside cleanup area: " + pos);
            }
            else if (pos.getY() < 0 || pos.getY() >= getWorld().getHeight())
            {
                fail.accept("Placed block outside map area: " + pos);
            }
        };
    }
}
