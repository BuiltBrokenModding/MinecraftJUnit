package com.builtbroken.mc.testing.junit;

import com.google.common.io.Files;
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
import java.util.*;

/**
* Test runner for Voltz Engine Unit tests. 
* As a note for Minecraft Mod developers. You are welcome to copy this Unit test and included it in your projects. So long
* as you provide credit to any develop that works on the file. As well do not profit off the project or violate any EULA or laws.
*
* @author Darkguardsman
*/
public class VoltzTestRunner extends Runner
{
    protected Class<? extends AbstractTest> clazz;
    protected LaunchClassLoader loader;
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
            // Setup data
            Object[] data = new Object[]{"", "", "", "", "1.7.10", "", Files.createTempDir(), Collections.EMPTY_LIST};
            URL[] urLs = ((URLClassLoader) Launch.class.getClassLoader()).getURLs();
            loader = new LaunchClassLoader(urLs);

            Class<?> itemz = loader.loadClass("cpw.mods.fml.relauncher.FMLRelaunchLog");
            Field mz = itemz.getDeclaredField("side");
            mz.setAccessible(true);

            mz.set(itemz, Enum.valueOf((Class<Enum>) mz.getType(), "CLIENT"));

            Class<?> item1 = loader.loadClass("cpw.mods.fml.common.Loader");
            Method m1 = item1.getMethod("injectData", Object[].class);
            m1.invoke(null, new Object[]{data});

            test_class = loader.loadClass(clazz.getName());
            test = test_class.newInstance();

            setUpClass = getMethod(test_class, "setUpForEntireClass");
            setUp = getMethod(test_class, "setUpForTest", String.class);
            tearDownClass = getMethod(test_class, "tearDownForEntireClass");
            tearDown = getMethod(test_class, "tearDownForTest", String.class);

            for (Method method : test_class.getMethods())
            {
                String name = method.getName();
                Annotation an = method.getAnnotation(Test.class);
                if (an != null || name.startsWith("test"))
                {
                    testMethods.put(method, Description.createTestDescription(test_class, method.getName()));
                }
            }

        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
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
                    cause = new RuntimeException("Test " + name + " has failed", cause);
                    Failure failure = new Failure(entry.getValue(), cause);
                    notifier.fireTestFailure(failure);
                }
                notifier.fireTestFinished(entry.getValue());
            }

            //Clean up data for the entire test class
            tearDownClass.invoke(test);

        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        notifier.fireTestRunFinished(new Result());
    }

    public static Method getMethod(Class clazz, String name, Class<?>... args)
    {
        Method method = null;
        try
        {
            method = clazz.getMethod(name, args);
        } catch (NoSuchMethodException e)
        {
            try
            {
                method = clazz.getDeclaredMethod(name, args);
            } catch (NoSuchMethodException e2)
            {

            }
        }
        return method;
    }
}