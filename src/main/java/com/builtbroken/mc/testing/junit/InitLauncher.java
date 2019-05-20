package com.builtbroken.mc.testing.junit;

import com.google.common.io.Files;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;

/**
 * Test runner for Voltz Engine Unit tests.
 * As a note for Minecraft Mod developers. You are welcome to copy this Unit test and included it in your projects. So long
 * as you provide credit to any develop that works on the file. As well do not profit off the project or violate any EULA or laws.
 *
 * @author Darkguardsman
 */
public class InitLauncher implements TestInstancePostProcessor
{
    //https://www.baeldung.com/junit-5-extensions

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception
    {
        try
        {
            Object[] data = new Object[]{"", "", "", "", "1.12.2", "", Files.createTempDir(), Collections.EMPTY_LIST};

            URL[] urLs = ((URLClassLoader) Launch.class.getClassLoader()).getURLs();
            LaunchClassLoader loader = new LaunchClassLoader(urLs);

            //Inject data into FML Loader
            Class<?> fmlLoader = loader.loadClass("net.minecraftforge.fml.common.Loader");
            Method injectDataMethod = fmlLoader.getMethod("injectData", Object[].class);
            injectDataMethod.invoke(null, new Object[]{data});

            Field namedModsField = fmlLoader.getDeclaredField("namedMods");
            namedModsField.setAccessible(true);
            namedModsField.set(fmlLoader.getMethod("instance").invoke(null), new HashMap<String, ModContainer>());

            //Load blocks and items
            try
            {
                Class<?> bootstrap = loader.loadClass("net.minecraft.init.Bootstrap");
                bootstrap.getMethod("register").invoke(null);
            } catch (Exception e)
            {
                e.printStackTrace();
                if (e instanceof NullPointerException)
                {
                    System.out.println("BlockReg: " + Block.REGISTRY + "  Size: " + Block.REGISTRY.getKeys().size());

                    Field[] fields = Blocks.class.getDeclaredFields();
                    System.out.println("Fields: " + fields.length);
                    for (Field field : fields)
                    {
                        try
                        {
                            System.out.println(field.getName() + ": " + field.get(null));
                        } catch (IllegalAccessException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
                throw new RuntimeException(e);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}