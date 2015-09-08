package com.builtbroken.mc.testing.junit.testers;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TestPlayer extends EntityPlayerMP
{
    public TestPlayer(MinecraftServer server, WorldServer world, GameProfile name)
    {
        super(server, world, name, new ItemInWorldManager(world));
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) { return false; }

    @Override
    public void addChatComponentMessage(IChatComponent chatmessagecomponent) {}

    @Override
    public void addStat(StatBase par1StatBase, int par2) {}

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {}

    public void reset()
    {
        this.setLocationAndAngles(0, 0, 0, 0, 0);
        this.inventory.mainInventory = new ItemStack[36];
        this.inventory.armorInventory = new ItemStack[4];
        this.inventory.currentItem = 0;
    }
}