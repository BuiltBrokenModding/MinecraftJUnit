package com.builtbroken.mc.testing.junit.world;

import com.builtbroken.mc.testing.junit.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Fake world that creates a single chunk to work out of
 * Created by robert on 11/20/2014.
 */
public class FakeWorld extends World
{
    public static boolean blocksInit = false;
    public List<TileEntity> tiles = new ArrayList<TileEntity>();

    Data[][][] mapData;
    int size;

    public FakeWorld()
    {
        this(50);
    }

    public FakeWorld(int size)
    {
        super(null, "FakeWorld", new FakeWorldProvider(), new WorldSettings(new WorldInfo(new NBTTagCompound())), new Profiler());
        this.size = size;
        mapData = new Data[size + size + 1][256][size + size + 1];
        ModRegistry.init();
    }

    @Override
    public void updateEntities()
    {
        Iterator<TileEntity> tile_iterator = tiles.iterator();
        while (tile_iterator.hasNext())
        {
            TileEntity tile = tile_iterator.next();
            if (tile.isInvalid())
            {
                tile_iterator.remove();
            }
            else
            {
                tile.updateEntity();
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        if (inMap(x, y, z))
        {
            return get(x, y, z).block;
        }
        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify)
    {
        boolean added_flag = false;
        boolean change_flag = false;
        if (block == null)
        {
            throw new NullPointerException("World.setBlock() can not set a location to null, use Blocks.Air in value of null");
        }

        if (inMap(x, y, z))
        {
            Block pre_block = getBlock(x, y, z);
            if (block != pre_block || getBlockMetadata(x, y, z) != meta)
            {
                get(x, y, z).block = block;
                get(x, y, z).meta = meta;
                change_flag = true;
                if (block != pre_block)
                {
                    added_flag = true;
                }

                //Check if tile changed
                TileEntity newTile = block != null ? block.createTileEntity(this, meta) : null;
                if (newTile != get(x, y, z).tile)
                {
                    if (get(x, y, z).tile != null)
                        get(x, y, z).tile.invalidate();
                    get(x, y, z).tile = newTile;
                    if (newTile != null)
                    {
                        newTile.setWorldObj(this);
                        newTile.xCoord = x;
                        newTile.yCoord = y;
                        newTile.zCoord = z;
                        newTile.validate();
                        if (newTile.canUpdate())
                            tiles.add(newTile);
                    }
                }
                if (added_flag)
                    block.onBlockAdded(this, x, y, z);
                if (change_flag && notify != 0)
                    notifyBlockChange(x, y, z, block);
            }

            return true;
        }
        else
        {
            throw new RuntimeException("Something Attempted to place a block out side of the test area " + x + "x " + y + "y " + z + "z");
        }
    }

    public int getBlockMetadata(int x, int y, int z)
    {
        if (inMap(x, y, z))
        {
            return get(x, y, z).meta;
        }
        return 0;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int n)
    {
        if (inMap(x, y, z))
        {
            get(x, y, z).meta = meta;
            notifyBlockChange(x, y, z, get(x, y, z).block);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        if (inMap(x, y, z))
        {
            return get(x, y, z).tile;
        }
        return null;
    }

    @Override
    public void notifyBlockOfNeighborChange(int x, int y, int z, final Block block)
    {
        Block b = this.getBlock(x, y, z);
        if (b != null)
        {
            b.onNeighborBlockChange(this, x, y, z, block);
        }
    }

    /**
     * Checks if the location is inside the map bounds
     */
    private boolean inMap(int x, int y, int z)
    {
        return x >= -size && x < mapData.length - size && y >= -size && y < mapData[0].length - size && z >= -size && z < mapData[0][0].length - size;
    }

    public Data get(int x, int y, int z)
    {
        if (mapData[x + size][y + size][z + size] == null)
        {
            mapData[x + size][y + size][z + size] = new Data();
            mapData[x + size][y + size][z + size].block = Blocks.air;
        }
        return mapData[x + size][y + size][z + size];
    }

    public void genFlatData()
    {
        for (int x = -size; x < mapData.length - size; x++)
        {
            for (int y = -size; x < mapData[0].length - size; x++)
            {
                for (int z = -size; x < mapData[0][0].length - size; x++)
                {
                    if (y == 0)
                        setBlock(x, y, z, Blocks.bedrock);
                    else if (y < 5)
                        setBlock(x, y, z, Blocks.stone);
                    else if (y < 10)
                        setBlock(x, y, z, Blocks.dirt);
                    else if (y < 11)
                        setBlock(x, y, z, Blocks.grass);
                    else
                        break;
                }
            }
        }
    }

    public void clear()
    {
        for (int x = 0; x < mapData.length; x++)
        {
            for (int y = 0; x < mapData[0].length; x++)
            {
                for (int z = 0; x < mapData[0][0].length; x++)
                {
                    Data data = mapData[x][y][x];
                    if (data != null)
                    {
                        if (data.tile != null)
                        {
                            data.tile.invalidate();
                            data.tile.setWorldObj(null);
                            data.tile = null;
                        }
                        data.block = null;
                    }
                }
            }
        }
    }

    public void printLevel(int y)
    {
        for (int x = 0; x < mapData.length; x++)
        {
            for (int z = 0; x < mapData[0][0].length; x++)
            {
                Data data = mapData[x][y][x];
                if (data != null)
                {
                    System.out.println("Data[" + data.block + ", " + data.meta + "]");
                }
            }
        }
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return null;
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

    /**
     * Holds basic data for the fake chunk
     */
    public static class Data
    {
        public Block block = null;
        public TileEntity tile = null;
        public int meta = 0;
    }
}
