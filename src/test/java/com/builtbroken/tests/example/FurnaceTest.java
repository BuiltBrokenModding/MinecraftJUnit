package com.builtbroken.tests.example;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tyler
 */
@RunWith(VoltzTestRunner.class)
public class FurnaceTest extends AbstractTest {
    World world;

    @Override
    public void setUpForEntireClass() {
        world = FakeWorld.newWorld("FurnaceTest");
    }

    @Test
    public void testFurnacePlacement()
    {
        if (Blocks.FURNACE != null)
        {
            BlockPos pos = new BlockPos(0, 0, 0);
            world.setBlockState(pos, Blocks.FURNACE.getDefaultState());
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            assertEquals("Should be a furnace.", block, Blocks.FURNACE);
            TileEntity tile = world.getTileEntity(pos);
            assertSame("World.getTileEntity() should have returned a furnace tile. Actually got " + tile, tile.getClass(), TileEntityFurnace.class);
        } else
        {
            fail("Blocks.furnace is null.");
        }
    }

    @Test
    public void testFurnaceRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.FURNACE.getDefaultState());
        world.setBlockToAir(pos);
        world.updateEntities();
        assertTrue("Tile should have been removed", world.getTileEntity(pos) == null);
    }

    /*@Test
    public void testFurnaceSmelt()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.furnace.getDefaultState());
        TileEntityFurnace furnace = (TileEntityFurnace) world.getTileEntity(pos);

        ItemStack coal = new ItemStack(Items.coal);
        ItemStack ore = new ItemStack(Blocks.iron_ore);
        ItemStack ingot = new ItemStack(Items.iron_ingot);

        NBTTagCompound compound = furnace.getTileData();
        int cookTime;

        // 0 input, 1 fuel, 2 output
        furnace.setInventorySlotContents(0, ore);
        furnace.setInventorySlotContents(1, coal);
        for (int i = 0; i < 2400; i++)
        {
            world.tick();
            cookTime = compound.getInteger("CookTime");
            if (cookTime > 0)
            {
                System.out.println(cookTime);
                if (furnace.getStackInSlot(2) != null)
                {
                    break;
                }
            }
        }
        assertEquals("Output should have been an Iron Ingot.", ingot, furnace.getStackInSlot(2));
    }*/
}
