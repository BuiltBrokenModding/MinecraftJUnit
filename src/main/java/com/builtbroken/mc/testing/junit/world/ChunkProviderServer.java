package com.builtbroken.mc.testing.junit.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

public class ChunkProviderServer implements IChunkProvider
{
    public IChunkProvider currentChunkProvider;
    public LongHashMap loadedChunkHashMap = new LongHashMap();
    public World worldObj;

    public ChunkProviderServer(World p_i1520_1_, IChunkProvider p_i1520_3_)
    {
        this.worldObj = p_i1520_1_;
        this.currentChunkProvider = p_i1520_3_;
    }

    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return this.loadedChunkHashMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }

    @Override
    public Chunk provideChunk(BlockPos pos)
    {
        int par1 = pos.getX();
        int par2 = pos.getZ();
        Chunk chunk = new FakeChunk(this.worldObj, par1, par2);
        chunk.setChunkLoaded(true);
        this.loadedChunkHashMap.add(ChunkCoordIntPair.chunkXZ2Int(par1, par2), chunk);
        return (Chunk) this.loadedChunkHashMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        BlockPos pos  = new BlockPos(x, 0, z);
        Chunk chunk = (Chunk) this.loadedChunkHashMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return chunk == null ? this.provideChunk(pos) : chunk;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {
        Chunk chunk = this.provideChunk(p_73153_2_, p_73153_3_);

        if (!chunk.isTerrainPopulated())
        {
            chunk.func_150809_p();

            if (this.currentChunkProvider != null)
            {
                this.currentChunkProvider.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                //TODO GameRegistry.generateWorld(p_73153_2_, p_73153_3_, worldObj, currentChunkProvider, p_73153_1_);
                chunk.setChunkModified();
            }
        }
    }

    @Override
    public boolean populateChunk(IChunkProvider chunkProvider, Chunk p_177460_2_, int x, int z)
    {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    @Override
    public boolean canSave()
    {
        return false;
    }

    @Override
    public String makeString()
    {
        return "ServerChunkCache: " + this.loadedChunkHashMap.getNumHashElements();
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return this.currentChunkProvider.getPossibleCreatures(creatureType, pos);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
    {
        return this.currentChunkProvider.getStrongholdGen(worldIn, structureName, position);
    }

    @Override
    public int getLoadedChunkCount()
    {
        return this.loadedChunkHashMap.getNumHashElements();
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {}

    @Override
    public void saveExtraData()
    {

    }
}