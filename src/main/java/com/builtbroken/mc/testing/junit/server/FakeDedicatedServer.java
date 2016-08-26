package com.builtbroken.mc.testing.junit.server;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommand;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.PropertyManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FakeDedicatedServer extends net.minecraft.server.dedicated.DedicatedServer
{
    private static final Logger logger = LogManager.getLogger();
    public final List pendingCommandList = Collections.synchronizedList(new ArrayList());

    public FakeDedicatedServer(File file)
    {
        super(file);
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        //http://stackoverflow.com/questions/15370120/get-thread-by-name
        for (Thread t : threadArray) {
            if (t.getName().equals("Server Infinisleeper")) {
                System.out.println("Killed 'Server Infinisleeper' thread");
                t.stop();
            }
        }

        logger.info("Starting fake server, Nothing will load or tick");

        this.setHostname("127.0.0.1");
        this.setServerPort(25565);
        this.setConfigManager(new DedicatedPlayerList(this));
        this.isAnnouncingPlayerAchievements();
        this.isCommandBlockEnabled();
        this.getOpPermissionLevel();
        this.isSnooperEnabled();
        this.setBuildLimit(256);
        this.settings = new PropertyManager(new File("fakeserver.properties"));
    }

    @Override
    protected boolean startServer() throws IOException
    {
        return true;
    }

    @Override
    public int getIntProperty(String p_71327_1_, int p_71327_2_)
    {
        return p_71327_2_;
    }

    @Override
    public String getStringProperty(String p_71330_1_, String p_71330_2_)
    {
        return p_71330_2_;
    }

    @Override
    public boolean getBooleanProperty(String p_71332_1_, boolean p_71332_2_)
    {
        return p_71332_2_;
    }

    @Override
    public void setProperty(String p_71328_1_, Object p_71328_2_)
    {

    }

    @Override
    public void saveProperties()
    {

    }


    public int getOpPermissionLevel()
    {
        return 4;
    }

    @Override
    public void setPlayerIdleTimeout(int idleTimeout) {

    }

    @Override
    public boolean canStructuresSpawn()
    {
        return false;
    }

    @Override
    public boolean isAnnouncingPlayerAchievements() {
        return false;
    }

    @Override
    protected boolean convertFiles() throws IOException
    {
        return false;
    }

    @Override
    public boolean isCommandBlockEnabled()
    {
        return false;
    }

    @Override
    public int getSpawnProtectionSize()
    {
        return 0;
    }


    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport p_71230_1_)
    {
        return p_71230_1_;
    }

    @Override
    public boolean getAllowNether()
    {
        return false;
    }

    @Override
    public boolean allowSpawnMonsters()
    {
        return false;
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_)
    {

    }

    @Override
    public boolean isSnooperEnabled()
    {
        return false;
    }

    @Override
    public void addPendingCommand(String p_71331_1_, ICommandSender p_71331_2_)
    {
        this.pendingCommandList.add(new ServerCommand(p_71331_1_, p_71331_2_));
    }

    @Override
    public void executePendingCommands()
    {
        while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand servercommand = (ServerCommand) this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(servercommand.sender, servercommand.command);
        }
    }
}
