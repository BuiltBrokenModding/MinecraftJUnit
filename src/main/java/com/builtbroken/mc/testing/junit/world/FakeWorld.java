package com.builtbroken.mc.testing.junit.world;

import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.GameType;
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
    public FakeWorld(FakeWorldSaveHandler saveHandler, WorldSettings settings, WorldProvider worldProvider, boolean client) {
        super(saveHandler, new WorldInfo(settings, "FakeWorld"), worldProvider, new Profiler(), client);
        provider.setWorld(this);
    }

    public static FakeWorld newWorld(String name)
    {
        WorldSettings settings = new WorldSettings(0, GameType.SURVIVAL, false, false, WorldType.FLAT);
        WorldInfo worldInfo = new WorldInfo(settings, name);
        FakeWorldSaveHandler handler = new FakeWorldSaveHandler(worldInfo);
        return new FakeWorld(handler, settings, new FakeWorldProvider(), true);
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return new ChunkProviderServer(this, this.saveHandler.getChunkLoader(this.provider), provider.createChunkGenerator());
    }

    @Override
    public Entity getEntityByID(int p_73045_1_)
    {
        return null;
    }
}
