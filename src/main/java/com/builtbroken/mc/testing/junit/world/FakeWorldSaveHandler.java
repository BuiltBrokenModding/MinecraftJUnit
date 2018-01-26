package com.builtbroken.mc.testing.junit.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import java.io.File;

/**
 * Created by Dark on 8/23/2015.
 */
public class FakeWorldSaveHandler implements ISaveHandler
{
    WorldInfo info;
    File dataDir;

    public FakeWorldSaveHandler(WorldInfo info)
    {
        this.info = info;
        dataDir = new File(new File("."), "data");
        dataDir.mkdirs();
    }

    @Override
    public WorldInfo loadWorldInfo()
    {
        return info;
    }

    @Override
    public void checkSessionLock() throws MinecraftException
    {

    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider)
    {
        return new FakeChunkLoader();
    }

    @Override
    public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_)
    {

    }

    @Override
    public void saveWorldInfo(WorldInfo p_75761_1_)
    {

    }

    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return null;
    }

    @Override
    public void flush()
    {

    }

    @Override
    public File getWorldDirectory()
    {
        return dataDir;
    }

    @Override
    public File getMapFileFromName(String s)
    {
        return new File(dataDir, s);
    }

    @Override
    public TemplateManager getStructureTemplateManager() {
        return new TemplateManager("MinecraftUnitTesting", new DataFixer(1));
    }
}
