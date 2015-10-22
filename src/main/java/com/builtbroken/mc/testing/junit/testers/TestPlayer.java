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

/**
 * Version of the player designed for JUnit testing. Needs a server instance, server world, and profile data to be created.
 */
public class TestPlayer extends EntityPlayerMP
{
    /** Toggle to print received chat messages to console. */
    public boolean outputChat = false;
    /** Toggle to throw errors when chat messages are received, useful for checking if chat messages are outputted correctly... or at all. */
    public boolean throwErrorsWhenReceivingChat = false;

    /**
     * Only constructor for this class
     *
     * @param server  - valid server instance, you should only create one for all of your tests
     * @param world   - server world, needs its own dimID and needs to be registered
     * @param profile = profile data, UUID is not required but name is required
     */
    public TestPlayer(MinecraftServer server, WorldServer world, GameProfile profile)
    {
        super(server, world, profile, new ItemInWorldManager(world));
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) { return false; }

    @Override
    public void addChatComponentMessage(IChatComponent chatmessagecomponent)
    {
        if (outputChat)
            System.out.println("[ToPlayer]: " + chatmessagecomponent.getFormattedText());
        if (throwErrorsWhenReceivingChat)
            throw new RuntimeException(chatmessagecomponent.getFormattedText());
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {}

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {}

    /**
     * Clears the data assigned to the test player.
     * <p/>
     * Set location to 0x 0y 0z 0yaw 0 pitch
     * Clears inventory
     * Clears armor
     * Set current item to 0
     */
    public void reset()
    {
        this.setLocationAndAngles(0, 0, 0, 0, 0);
        this.inventory.mainInventory = new ItemStack[36];
        this.inventory.armorInventory = new ItemStack[4];
        this.inventory.currentItem = 0;
    }
}