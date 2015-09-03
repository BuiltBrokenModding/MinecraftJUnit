package com.builtbroken.mc.testing.junit.world;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import java.io.File;

/**
 * Version of WorldServer for unit testing
 * Created by Dark on 9/3/2015.
 */
public class FakeWorldServer extends WorldServer
{
    public static File baseFolder = new File(new File("."), "out/test/FakeWorldWithServer/");
    File rootFolder;

    public FakeWorldServer(String name, MinecraftServer server, File file, FakeWorldSaveHandler handler, int dimID, WorldSettings settings)
    {
        super(server, handler, name, dimID, settings, new Profiler());
        rootFolder = file;
    }

    public static FakeWorldServer newWorld(String name)
    {
        return newWorld(new FakeDedicatedServer(new File(baseFolder, name)), name);
    }

    public static FakeWorldServer newWorld(MinecraftServer server, String name)
    {
        WorldSettings settings = new WorldSettings(0, WorldSettings.GameType.SURVIVAL, false, false, WorldType.FLAT);
        WorldInfo worldInfo = new WorldInfo(settings, name);
        FakeWorldSaveHandler handler = new FakeWorldSaveHandler(worldInfo);
        if (DimensionManager.getWorld(0) == null)
        {
            WorldSettings settings2 = new WorldSettings(0, WorldSettings.GameType.SURVIVAL, false, false, WorldType.FLAT);
            WorldInfo worldInfo2 = new WorldInfo(settings, name);
            FakeWorldSaveHandler handler2 = new FakeWorldSaveHandler(worldInfo2);

            DimensionManager.setWorld(0, new FakeWorldServer("overworld", server, new File(baseFolder, name), handler2, 0, settings2));
        }
        if (!DimensionManager.isDimensionRegistered(10))
        {
            DimensionManager.registerProviderType(10, FakeWorldProvider.class, false);
            DimensionManager.registerDimension(10, 10);
        }
        return new FakeWorldServer(name, server, new File(baseFolder, name), handler, 10, settings);
    }

    @Override
    public File getChunkSaveLocation()
    {
        return new File(rootFolder, "save");
    }
}
