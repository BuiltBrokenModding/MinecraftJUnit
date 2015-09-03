package com.builtbroken.mc.testing.junit.world;

import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

/**
 * Fake world that creates a single chunk to work out of
 * Created by robert on 11/20/2014.
 */
public class FakeWorld extends AbstractFakeWorld
{
    protected WorldInfo worldInfo;
    protected WorldSettings settings;

    public FakeWorld(FakeWorldSaveHandler handler, WorldSettings settings, WorldInfo info, WorldProvider provider)
    {
        super(handler, "FakeWorld", settings, provider, new Profiler());
        this.worldInfo = info;
    }

    public static FakeWorld newWorld(String name)
    {
        WorldSettings settings = new WorldSettings(0, WorldSettings.GameType.SURVIVAL, false, false, WorldType.FLAT);
        WorldInfo worldInfo = new WorldInfo(settings, name);
        FakeWorldSaveHandler handler = new FakeWorldSaveHandler(worldInfo);
        return new FakeWorld(handler, settings, worldInfo, new FakeWorldProvider());
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return new ChunkProviderServer(this, new ChunkProviderEmpty(this));
    }

    @Override
    protected int func_152379_p()
    {
        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_)
    {
        return null;
    }
}
