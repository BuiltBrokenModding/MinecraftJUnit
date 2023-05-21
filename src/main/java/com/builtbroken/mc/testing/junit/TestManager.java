package com.builtbroken.mc.testing.junit;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import com.mojang.authlib.GameProfile;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Dark(DarkGuardsman, Robert) on 12/31/2019.
 */
public class TestManager
{
    private final Map<Integer, FakeWorldServer> worlds = new HashMap<>();
    private final Map<GameProfile, TestPlayer> players = new HashMap<>();
    private FakeDedicatedServer server;

    private final String name;
    private final Consumer<String> errorHandler;

    /** Used to wrap worlds, useful for setting up mockito spies */
    private Function<FakeWorldServer, FakeWorldServer> worldWrapper;
    /** Used to wrap worlds, useful for setting up mockito spies */
    private Function<TestPlayer, TestPlayer> playerWrapper;

    public TestManager(String name, Consumer<String> errorHandler)
    {
        this.name = name;
        this.errorHandler = errorHandler;
    }

    public TestManager withWorldWrapper(Function<FakeWorldServer, FakeWorldServer> worldWrapper) {
        this.worldWrapper = worldWrapper;
        return this;
    }

    public TestManager withPlayerWrapper(Function<TestPlayer, TestPlayer> wrapper) {
        this.playerWrapper = wrapper;
        return this;
    }

    public FakeDedicatedServer getServer()
    {
        if (server == null)
        {
            Bootstrap.register();
            FakeDedicatedServer.exceptionHandler = errorHandler;

            server = FakeWorldServer.createServer(name);
        }
        return server;
    }

    @Deprecated
    public FakeWorldServer getWorld()
    {
        return getWorld(0);
    }

    public FakeWorldServer getWorld(int id)
    {
        initWorld(id);
        return worlds.get(id);
    }

    public void initWorld(int id) {
        if (!worlds.containsKey(id))
        {
            final FakeWorldServer world = FakeWorldServer.newWorld(getServer(), id, name);
            world.init();

            worlds.put(id, worldWrapper != null ? worldWrapper.apply(world) : world);
        }
    }

    @Deprecated
    public TestPlayer getPlayer() {
        return getPlayer(0, null);
    }

    public TestPlayer getPlayer(int worldId, GameProfile profile)
    {
        if (!players.containsKey(profile))
        {
            final TestPlayer player = new TestPlayer(getServer(), getWorld(worldId), profile);
            players.put(profile, playerWrapper != null ? playerWrapper.apply(player) : player);
        }
        return players.get(profile);
    }

    /**
     * Destroys everything at the end of the test
     */
    public void tearDownTest()
    {
        cleanupBetweenTests();
        getServer().dispose();
        cleanupBetweenTests();
    }

    /**
     * Cleans up data between tests
     */
    public void cleanupBetweenTests()
    {
        players.values().forEach(TestPlayer::reset);
        players.clear();

        worlds.values().forEach(world -> {
            DimensionManager.setWorld(world.provider.getDimension(), null, getServer());
        });
        worlds.clear();
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
