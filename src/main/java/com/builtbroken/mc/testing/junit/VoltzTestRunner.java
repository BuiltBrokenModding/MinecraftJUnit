package com.builtbroken.mc.testing.junit;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.google.common.io.Files;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test runner for Voltz Engine Unit tests.
 * As a note for Minecraft Mod developers. You are welcome to copy this Unit test and included it in your projects. So long
 * as you provide credit to any develop that works on the file. As well do not profit off the project or violate any EULA or laws.
 *
 * @author Darkguardsman
 */
public class VoltzTestRunner extends Runner
{
    public static LaunchClassLoader loader;
    public static FakeDedicatedServer server;

    protected Class<? extends AbstractTest> clazz;
    protected Object test;
    protected Class<?> test_class;
    protected HashMap<Method, Description> testMethods = new HashMap();

    protected Method setUpClass;
    protected Method setUp;
    protected Method tearDownClass;
    protected Method tearDown;

    public VoltzTestRunner(Class<? extends AbstractTest> clazz) throws InitializationError, ClassNotFoundException
    {
        this.clazz = clazz;
        init();
    }

    public void init()
    {
        try
        {
            Object[] data = new Object[]{"", "", "", "", "1.7.10", "", Files.createTempDir(), Collections.EMPTY_LIST};
            // Setup data
            if (loader == null)
            {
                URL[] urLs = ((URLClassLoader) Launch.class.getClassLoader()).getURLs();
                loader = new LaunchClassLoader(urLs);

                //Set side to client
                Class<?> fmlRelaunchLogClass = loader.loadClass("cpw.mods.fml.relauncher.FMLRelaunchLog");
                Field sideField = fmlRelaunchLogClass.getDeclaredField("side");
                sideField.setAccessible(true);
                sideField.set(fmlRelaunchLogClass, Enum.valueOf((Class<Enum>) sideField.getType(), "CLIENT"));

                //Inject data into FML Loader
                Class<?> fmlLoader = loader.loadClass("cpw.mods.fml.common.Loader");
                Method injectDataMethod = fmlLoader.getMethod("injectData", Object[].class);
                injectDataMethod.invoke(null, new Object[]{data});

                Field namedModsField = fmlLoader.getDeclaredField("namedMods");
                namedModsField.setAccessible(true);
                namedModsField.set(fmlLoader.getMethod("instance").invoke(null), new HashMap<String, ModContainer>());

                //Load blocks and items
                try
                {
                    Class<?> bootstrap = loader.loadClass("net.minecraft.init.Bootstrap");
                    bootstrap.getMethod("func_151354_b").invoke(null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    if (e instanceof NullPointerException)
                    {
                        System.out.println("BlockReg: " + Block.blockRegistry + "  Size: " + Block.blockRegistry.getKeys().size());

                        Field[] fields = Blocks.class.getDeclaredFields();
                        System.out.println("Fields: " + fields.length);
                        for (Field field : fields)
                        {
                            try
                            {
                                System.out.println(field.getName() + ": " + field.get(null));
                            }
                            catch (IllegalAccessException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                    throw new RuntimeException(e);
                }
            }

            test_class = loader.loadClass(clazz.getName());
            test = test_class.newInstance();

            setUpClass = getMethod(test_class, "setUpForEntireClass");
            setUp = getMethod(test_class, "setUpForTest", String.class);
            tearDownClass = getMethod(test_class, "tearDownForEntireClass");
            tearDown = getMethod(test_class, "tearDownForTest", String.class);

            for (Method method : test_class.getMethods())
            {
                if (!testMethods.containsKey(method))
                {
                    String name = method.getName();
                    Annotation an = method.getAnnotation(Test.class);
                    if (an != null || name.startsWith("test"))
                    {
                        testMethods.put(method, Description.createTestDescription(test_class, method.getName()));
                    }
                }
            }

        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Description getDescription()
    {
        Description description = Description.createSuiteDescription(clazz + "-Suit");
        for (Map.Entry<Method, Description> entry : testMethods.entrySet())
        {
            description.addChild(entry.getValue());
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier)
    {
        try
        {
            setUpClass.invoke(test);

            for (Map.Entry<Method, Description> entry : testMethods.entrySet())
            {
                Method method = entry.getKey();
                String name = entry.getKey().getName();
                notifier.fireTestStarted(entry.getValue());
                try
                {
                    setUp.invoke(test, name);
                    method.invoke(test);
                    tearDown.invoke(test, name);
                }
                catch (Exception e)
                {
                    Throwable cause = e;
                    if (e instanceof InvocationTargetException)
                    {
                        cause = e.getCause();
                    }
                    System.out.println("\n\nTest " + name + " has failed");
                    Failure failure = new Failure(entry.getValue(), cause);
                    notifier.fireTestFailure(failure);
                }
                notifier.fireTestFinished(entry.getValue());
            }

            //Clean up data for the entire test class
            tearDownClass.invoke(test);

        }
        catch (Exception e)
        {
            notifier.fireTestFailure(new Failure(Description.createSuiteDescription(test_class), e));
        }
        notifier.fireTestRunFinished(new Result());
    }

    public static Method getMethod(Class clazz, String name, Class<?>... args)
    {
        Method method = null;
        try
        {
            method = clazz.getMethod(name, args);
        }
        catch (NoSuchMethodException e)
        {
            try
            {
                method = clazz.getDeclaredMethod(name, args);
            }
            catch (NoSuchMethodException e2)
            {

            }
        }
        return method;
    }
}