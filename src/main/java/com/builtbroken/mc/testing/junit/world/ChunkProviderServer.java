package com.builtbroken.mc.testing.junit.world;

<<<<<<< HEAD
import net.minecraft.util.math.ChunkPos;
=======
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;

public class ChunkProviderServer implements IChunkProvider
{
    public final Long2ObjectMap<Chunk> MAP = new Long2ObjectOpenHashMap<Chunk>(8192);
    private IChunkGenerator generator;
    private IChunkLoader loader;
    private World world;

    public ChunkProviderServer(World world, IChunkLoader loader, IChunkGenerator generator) {
        this.world = world;
        this.loader = loader;
        this.generator = generator;
    }

    @Nullable
    @Override
<<<<<<< HEAD
    public Chunk getLoadedChunk(int x, int z) {
        long l = ChunkPos.chunkXZ2Int(x, z);
        Chunk chunk = MAP.get(l);
        if (chunk != null) {
            chunk.unloaded = false;
=======
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
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
        }
        return chunk;
    }

    @Override
<<<<<<< HEAD
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = getLoadedChunk(x, z);
        if (chunk == null) {
            long l = ChunkPos.chunkXZ2Int(x, z);
=======
    public boolean populateChunk(IChunkProvider chunkProvider, Chunk p_177460_2_, int x, int z)
    {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450

            chunk = generator.provideChunk(x, z);
            MAP.put(l, chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, generator);
        }
        return chunk;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
<<<<<<< HEAD
    public String makeString() {
        return "ServerChunkCache: " + MAP.size();
=======
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

>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
    }
}