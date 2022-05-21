package com.builtbroken.mc.testing.junit.server;

import com.builtbroken.mc.testing.junit.helpers.ReflectionHelpers;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@SideOnly(Side.SERVER)
public class FakeDedicatedServer extends DedicatedServer
{
    public static Consumer<String> exceptionHandler = (string) -> new RuntimeException(string);

    private static final Logger logger = LogManager.getLogger();

    private static YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
    private static MinecraftSessionService sessionService = service.createMinecraftSessionService();
    private static GameProfileRepository profileRepository = service.createProfileRepository();

    private static final File CACHE = new File("usercache.json");



    public FakeDedicatedServer(File file)
    {
        super(file, DataFixesManager.createFixer(), service, sessionService, profileRepository, new PlayerProfileCache(profileRepository, new File(file, CACHE.getName())));
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        //http://stackoverflow.com/questions/15370120/get-thread-by-name
        for (Thread t : threadArray)
        {
            if (t.getName().equals("Server Infinisleeper"))
            {
                System.out.println("Killed 'Server Infinisleeper' thread");
                t.stop();
            }
        }

        logger.info("Starting fake server, Nothing will load or tick");

        this.setHostname("127.0.0.1");
        this.setServerPort(25565);
        this.isCommandBlockEnabled();
        this.getOpPermissionLevel();
        this.isSnooperEnabled();
        this.setBuildLimit(256);
    }

    @Override
    public boolean init()
    {
        try
        {
            final PropertyManager settings = new PropertyManager(File.createTempFile("server", "properties"))
            {
                @Override
                public void generateNewProperties()
                {
                    //DO not save file to system
                    //LOGGER.info("Generating new properties file");
                    //this.saveProperties();
                }
            };

            //Override settings manager
            ReflectionHelpers.setPrivateField(DedicatedServer.class, "settings", this, settings);

            //Override player list
            this.setPlayerList(new FakeDedicatedPlayerList(this));

            if (this.isSinglePlayer())
            {
                this.setHostname("127.0.0.1");
            }
            else
            {
                this.setOnlineMode(settings.getBooleanProperty("online-mode", true));
                this.setPreventProxyConnections(settings.getBooleanProperty("prevent-proxy-connections", false));
                this.setHostname(settings.getStringProperty("server-ip", ""));
            }

            this.setCanSpawnAnimals(settings.getBooleanProperty("spawn-animals", true));
            this.setCanSpawnNPCs(settings.getBooleanProperty("spawn-npcs", true));
            this.setAllowPvp(settings.getBooleanProperty("pvp", true));
            this.setAllowFlight(settings.getBooleanProperty("allow-flight", false));
            this.setResourcePack(settings.getStringProperty("resource-pack", ""), this.loadResourcePackSHA());
            this.setMOTD(settings.getStringProperty("motd", "A Minecraft Server"));
            this.setForceGamemode(settings.getBooleanProperty("force-gamemode", false));
            this.setPlayerIdleTimeout(settings.getIntProperty("player-idle-timeout", 0));

            if (settings.getIntProperty("difficulty", 1) < 0)
            {
                settings.setProperty("difficulty", Integer.valueOf(0));
            }
            else if (settings.getIntProperty("difficulty", 1) > 3)
            {
                settings.setProperty("difficulty", Integer.valueOf(3));
            }

            //this.canSpawnStructures = settings.getBooleanProperty("generate-structures", true);
            int i = settings.getIntProperty("gamemode", GameType.SURVIVAL.getID());
            //this.gameType = WorldSettings.getGameTypeById(i);
            //LOGGER.info("Default game type: {}", (Object)this.gameType);
            //InetAddress inetaddress = null;

            if (!this.getServerHostname().isEmpty())
            {
                //inetaddress = InetAddress.getByName(this.getServerHostname());
            }

            if (this.getServerPort() < 0)
            {
                //this.setServerPort(settings.getIntProperty("server-port", 25565));
            }

            //LOGGER.info("Generating keypair");
            //this.setKeyPair(CryptManager.generateKeyPair());
            //LOGGER.info("Starting Minecraft server on {}:{}", this.getServerHostname().isEmpty() ? "*" : this.getServerHostname(), Integer.valueOf(this.getServerPort()));

            //try
            {
                //this.getNetworkSystem().addEndpoint(inetaddress, this.getServerPort());
            }
            //catch (IOException ioexception)
            {
                //LOGGER.warn("**** FAILED TO BIND TO PORT!");
                //LOGGER.warn("The exception was: {}", (Object)ioexception.toString());
                //LOGGER.warn("Perhaps a server is already running on that port?");
                //return false;
            }

            //if (!this.isServerInOnlineMode())
            //{
            //LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            //LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
            //LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            //LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
            //}

            //if (this.convertFiles())
            //{
            //this.getPlayerProfileCache().save();
            //}

            if (!PreYggdrasilConverter.tryConvert(settings))
            {
                return false;
            }
            else
            {
                //net.minecraftforge.fml.common.FMLCommonHandler.instance().onServerStarted();
                //this.setPlayerList(new FakeDedicatedPlayerList(this));
                long j = System.nanoTime();

                if (this.getFolderName() == null)
                {
                    this.setFolderName(settings.getStringProperty("level-name", "world"));
                }

                String s = settings.getStringProperty("level-seed", "");
                String s1 = settings.getStringProperty("level-type", "DEFAULT");
                String s2 = settings.getStringProperty("generator-settings", "");
                long k = (new Random()).nextLong();

                if (!s.isEmpty())
                {
                    try
                    {
                        long l = Long.parseLong(s);

                        if (l != 0L)
                        {
                            k = l;
                        }
                    } catch (NumberFormatException var16)
                    {
                        k = (long) s.hashCode();
                    }
                }

                WorldType worldtype = WorldType.parseWorldType(s1);

                if (worldtype == null)
                {
                    worldtype = WorldType.DEFAULT;
                }

                this.isCommandBlockEnabled();
                this.getOpPermissionLevel();
                this.isSnooperEnabled();
                this.getNetworkCompressionThreshold();
                this.setBuildLimit(settings.getIntProperty("max-build-height", 256));
                this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
                this.setBuildLimit(MathHelper.clamp(this.getBuildLimit(), 64, 256));
                settings.setProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
                //TileEntitySkull.setProfileCache(this.getPlayerProfileCache());
                //TileEntitySkull.setSessionService(this.getMinecraftSessionService());
                PlayerProfileCache.setOnlineMode(false);
                //if (!net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerAboutToStart(this)) return false;
                //LOGGER.info("Preparing level \"{}\"", (Object) this.getFolderName());
                //this.loadAllWorlds(this.getFolderName(), this.getFolderName(), k, worldtype, s2);
                long i1 = System.nanoTime() - j;
                String s3 = String.format("%.3fs", (double) i1 / 1.0E9D);
                //LOGGER.info("Done ({})! For help, type \"help\" or \"?\"", (Object) s3);
                this.currentTime = getCurrentTimeMillis();

                if (settings.hasProperty("announce-player-achievements"))
                {
                    this.worlds[0].getGameRules().setOrCreateGameRule("announceAdvancements", settings.getBooleanProperty("announce-player-achievements", true) ? "true" : "false");
                    settings.removeProperty("announce-player-achievements");
                    settings.saveProperties();
                }

                if (settings.getBooleanProperty("enable-query", false))
                {
                    //LOGGER.info("Starting GS4 status listener");
                    //this.rconQueryThread = new RConThreadQuery(this);
                    //this.rconQueryThread.startThread();
                }

                if (settings.getBooleanProperty("enable-rcon", false))
                {
                    //LOGGER.info("Starting remote control listener");
                    //this.rconThread = new RConThreadMain(this);
                    //this.rconThread.startThread();
                }

                Items.AIR.getSubItems(CreativeTabs.SEARCH, NonNullList.create());
                // <3 you Grum for this, saves us ~30 patch files! --^
                //return net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerStarting(this);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            exceptionHandler.accept("Failed to setup dedicated server due to error: " + e.getMessage());
        }
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
    public void setPlayerIdleTimeout(int idleTimeout)
    {

    }

    @Override
    public boolean canStructuresSpawn()
    {
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
    public void addServerStatsToSnooper(Snooper playerSnooper)
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
        //this.pendingCommandList.add(new ServerCommand(p_71331_1_, p_71331_2_));
    }

    @Override
    public void executePendingCommands()
    {
        /*while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand servercommand = (ServerCommand) this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(servercommand.sender, servercommand.command);
        }*/
    }

    public void dispose()
    {
        for (WorldServer world : this.worlds)
        {
            net.minecraftforge.common.DimensionManager.setWorld(world.provider.getDimension(), null, this);
        }
    }
}
