package com.builtbroken.tests.example;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.builtbroken.mc.testing.junit.prefabs.AbstractTest;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Tyler
 */
public class TestFurnace extends AbstractTest {
    static World world;

    @BeforeAll
    public static void setUpForEntireClass() {
        Bootstrap.register();
        world = FakeWorld.newWorld("TestFurnace");
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
            Assertions.assertEquals(block, Blocks.FURNACE, "Should be a furnace.");
            TileEntity tile = world.getTileEntity(pos);
            Assertions.assertSame(tile.getClass(), TileEntityFurnace.class, "World.getTileEntity() should have returned a furnace tile. Actually got " + tile);
        } else
        {
            Assertions.fail("Blocks.furnace is null.");
        }
    }

    @Test
    public void testFurnaceRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.FURNACE.getDefaultState());
        world.setBlockToAir(pos);
        world.updateEntities();
        Assertions.assertTrue(world.getTileEntity(pos) == null, "Tile should have been removed");
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
