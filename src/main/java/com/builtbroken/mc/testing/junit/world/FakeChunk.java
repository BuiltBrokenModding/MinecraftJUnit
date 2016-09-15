package com.builtbroken.mc.testing.junit.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
<<<<<<< HEAD
import net.minecraft.util.math.BlockPos;
=======
import net.minecraft.util.BlockPos;
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

/**
 * Created by Cow Pi on 8/24/2015.
 */
public class FakeChunk extends Chunk
{
    public FakeChunk(World p_i1995_1_, int p_i1995_2_, int p_i1995_3_)
    {
        super(p_i1995_1_, p_i1995_2_, p_i1995_3_);
    }

    public FakeChunk(World p_i45446_1_, ChunkPrimer p_i45446_2_, int p_i45446_3_, int p_i45446_4_)
    {
        super(p_i45446_1_, p_i45446_2_, p_i45446_3_, p_i45446_4_);
    }

    @Override
    public IBlockState setBlockState(BlockPos pos, IBlockState state)
    {
        debug(String.format("setBlockState(%s, %s)", pos, state));
        int x = pos.getX() & 15;
        int y = pos.getY();
        int k = pos.getZ() & 15;
        int z = k << 4 | x;

        if (y >= this.precipitationHeightMap[z] - 1)
        {
            this.precipitationHeightMap[z] = -999;
        }

        int i1 = this.heightMap[z];
        IBlockState iblockstate = this.getBlockState(pos);

        if (iblockstate == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate.getBlock();
            debug(String.format("New block: %s | Old block: %s", block, block1));
<<<<<<< HEAD
            int k1 = block1.getLightOpacity(iblockstate, this.worldObj, pos); // Relocate old light value lookup here, so that it is called before TE is removed.
=======
            int k1 = block1.getLightOpacity(this.worldObj, pos); // Relocate old light value lookup here, so that it is called before TE is removed.
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
            debug("  ExtendedBlockStorage[" + (y >> 4) + "] = " + extendedblockstorage);
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (block == Blocks.AIR)
                {
                    debug("Block is air.");
                    return null;
                }

                extendedblockstorage = this.storageArrays[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !this.worldObj.provider.getHasNoSky());
                flag = y >= i1;
            }

            extendedblockstorage.set(x, y & 15, k, state);

            //if (block1 != block)
            {
                if (!this.worldObj.isRemote)
                {
                    if (block1 != block) //Only fire block breaks when the block changes.
                        block1.breakBlock(this.worldObj, pos, iblockstate);
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate, state)) this.worldObj.removeTileEntity(pos);
                }
                else if (block1.hasTileEntity(iblockstate))
                {
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate, state)) {
                        this.worldObj.removeTileEntity(pos);
                    }
                }
            }

<<<<<<< HEAD
            /*if (extendedblockstorage.s(x, y & 15, k) != block)
            {
                return null;
            }
            else*/
=======
            if (extendedblockstorage.getBlockByExtId(x, y & 15, k) != block)
            {
                return null;
            }
            else
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
            {
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
<<<<<<< HEAD
                    int j1 = block.getLightOpacity(iblockstate, this.worldObj, pos);
=======
                    int j1 = block.getLightOpacity(this.worldObj, pos);
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450

                    if (j1 > 0)
                    {
                        if (y >= i1)
                        {
                            this.relightBlock(x, y + 1, k);
                        }
                    }
                    else if (y == i1 - 1)
                    {
                        this.relightBlock(x, y, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        this.propagateSkylightOcclusion(x, k);
                    }
                }

                if (!this.worldObj.isRemote && block1 != block)
                {
                    block.onBlockAdded(this.worldObj, pos, state);
                }

                if (block.hasTileEntity(state))
                {
                    TileEntity tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity1 == null)
                    {
                        tileentity1 = block.createTileEntity(this.worldObj, state);
                        this.worldObj.setTileEntity(pos, tileentity1);
                    }

                    if (tileentity1 != null)
                    {
                        tileentity1.updateContainingBlockInfo();
                    }
                }

                this.setModified(true);
                return iblockstate;
            }
        }
    }

    @Override
    public void removeTileEntity(BlockPos pos)
    {
        debug("removeTileEntity(" + pos + ")");

        if (this.isChunkLoaded)
        {
            TileEntity tileentity = (TileEntity) this.chunkTileEntityMap.remove(pos);
            debug(" removed tile = " + tileentity);
            if (tileentity != null)
            {
                tileentity.invalidate();
            }
        }
    }

    protected void debug(String msg)
    {
        if (getWorld() instanceof AbstractFakeWorld && ((AbstractFakeWorld) getWorld()).debugInfo)
            ((AbstractFakeWorld) getWorld()).logger.info(this + " " + msg);
    }

}
