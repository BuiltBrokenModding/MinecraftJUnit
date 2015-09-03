package com.builtbroken.mc.testing.junit.world;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommand;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.dedicated.DedicatedPlayerList;
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
        for (int i = 0; i < threadArray.length; i++)
        {
            if (threadArray[i].getName().equals("Server Infinisleeper"))
            {
                System.out.println("Killed 'Server Infinisleeper' thread");
                threadArray[i].stop();
            }
        }
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        logger.info("Starting fake server, Nothing will load or tick");

        this.setHostname("127.0.0.1");
        this.setServerPort(25565);


        if (this.func_152368_aE())
        {
            this.func_152358_ax().func_152658_c();
        }
        FMLCommonHandler.instance().onServerStarted();
        this.func_152361_a(new DedicatedPlayerList(this));

        this.func_147136_ar();
        this.isCommandBlockEnabled();
        this.getOpPermissionLevel();
        this.isSnooperEnabled();
        this.setBuildLimit(256);
        return true;
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
