package com.builtbroken.mc.testing.junit.testers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

/**
 * Version of the player designed for JUnit testing. Needs a server instance, server world, and profile data to be created.
 */
public class TestPlayer extends EntityPlayerMP
{

    /**
     * Toggle to print received chat messages to console.
     */
    public boolean outputChat = false;
    /**
     * Toggle to throw errors when chat messages are received, useful for checking if chat messages are outputted correctly... or at all.
     */
    public boolean throwErrorsWhenReceivingChat = false;
    /**
     * Toggle to throw errors when stats are received, useful for checking if stats are received correctly... or at all.
     */
    public boolean throwErrorsWhenReceivingStats = false;
    /**
     * Toggle to throw errors when guis are opened, useful for checking if guis are opened correctly... or at all.
     */
    public boolean throwErrorsWhenOpeningGUI = false;

    private static GameProfile PROFILE_DEFAULT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE76"), "[UNIT_TESTER]");

    /**
     * Only constructor for this class
     *
     * @param server  - valid server instance, you should only create one for all of your tests
     * @param world   - server world, needs its own dimID and needs to be registered
     * @param profile = profile data, UUID is not required but name is required
     */
    public TestPlayer(MinecraftServer server, WorldServer world, GameProfile profile)
    {
        super(server, world, profile, new PlayerInteractionManager(world));
    }

    public TestPlayer(MinecraftServer server, WorldServer world)
    {
        super(server, world, PROFILE_DEFAULT, new PlayerInteractionManager(world));
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2)
    {
        if (throwErrorsWhenReceivingStats)
        {
            throw new RuntimeException(par1StatBase + "  " + par2);
        }
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z)
    {
        if (throwErrorsWhenOpeningGUI)
        {
            throw new RuntimeException("mod:" + mod + " id:" + modGuiId + " dim:" + world.provider.getDimension() + " " + x + "x " + y + "y " + z + "z");
        }
    }

    @Override
    public void setGameType(GameType gameType)
    {
        this.interactionManager.setGameType(gameType);

        if (gameType == GameType.SPECTATOR)
        {
            this.spawnShoulderEntities();
            this.dismountRidingEntity();
        }
        else
        {
            this.setSpectatingEntity(this);
        }

        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }

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
        for (EntityEquipmentSlot slotIn : EntityEquipmentSlot.values())
        {
            this.setItemStackToSlot (slotIn, ItemStack.EMPTY);
        }
        inventory.clear();
        this.inventory.currentItem = 0;
        setGameType(GameType.NOT_SET);
    }
}