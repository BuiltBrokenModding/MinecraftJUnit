package com.builtbroken.mc.testing.junit.world;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Version of WorldServer for unit testing
 * Created by Dark on 9/3/2015.
 */
public class FakeWorldServer extends WorldServer
{

    public static File baseFolder = new File(new File("."), "out/test/FakeWorldWithServer/");
    File rootFolder;

    public BiConsumer<BlockPos, IBlockState> setBlockCallback;

    public FakeWorldServer(WorldInfo info, MinecraftServer server, File file, FakeWorldSaveHandler handler, int dimID, WorldSettings settings)
    {
        super(server, handler, info, dimID, new Profiler());
        rootFolder = file;
        initialize(settings);
    }

    public static FakeDedicatedServer createServer(String name)
    {
        FakeDedicatedServer server = new FakeDedicatedServer(new File(baseFolder, name));
        server.init();
        return server;
    }

    public static FakeWorldServer newWorld()
    {
        return newWorld(createServer("TestServer"), 0, "OVERWORLD");
    }

    public static FakeWorldServer newWorld(String name)
    {
        return newWorld(createServer(name), 0, name);
    }

    public static FakeWorldServer newWorld(MinecraftServer server, int dim, String name)
    {
        WorldSettings settings = new WorldSettings(0, GameType.SURVIVAL, false, false, WorldType.FLAT);
        WorldInfo worldInfo = new WorldInfo(settings, name);
        FakeWorldSaveHandler handler = new FakeWorldSaveHandler(worldInfo);

        //Check that there is no dim for world
        if (DimensionManager.getWorld(dim) != null)
        {
            final String worldName = DimensionManager.getWorld(dim).getWorldInfo() != null
                    ? DimensionManager.getWorld(dim).getWorldInfo().getWorldName()
                    : null;
            throw new RuntimeException("World was not cleaned up before test was run. " +
                    "Make sure to clear the test server between tests. " +
                    "World: " + worldName);
        }

        //Create world
        FakeWorldServer world = new FakeWorldServer(worldInfo, server, new File(baseFolder, name), handler, dim, settings);

        //Register world
        DimensionManager.setWorld(0, world, server);

        //Return
        return world;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        if (setBlockCallback != null)
        {
            setBlockCallback.accept(pos, newState);
        }
        return super.setBlockState(pos, newState, flags);
    }

    @Override
    public File getChunkSaveLocation()
    {
        return new File(rootFolder, "save");
    }
}
