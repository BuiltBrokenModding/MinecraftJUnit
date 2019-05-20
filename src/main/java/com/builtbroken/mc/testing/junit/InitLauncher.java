package com.builtbroken.mc.testing.junit;

import com.google.common.io.Files;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Field;;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * Test runner for Voltz Engine Unit tests.
 * As a note for Minecraft Mod developers. You are welcome to copy this Unit test and included it in your projects. So long
 * as you provide credit to any develop that works on the file. As well do not profit off the project or violate any EULA or laws.
 *
 * @author Darkguardsman
 */
@Deprecated
public class InitLauncher implements BeforeAllCallback
{
    //https://www.baeldung.com/junit-5-extensions

    private boolean started = false;
    static LaunchClassLoader loader;

    @Override
    public void beforeAll(ExtensionContext context)
    {
        //https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
        if(!started)
        {
            started = true;
            context.getRoot().getStore(GLOBAL).put("com.builtbroken.minecraft.bootloader", this);
            try
            {
                Object[] data = new Object[]{"", "", "", "", "1.12.2", "", Files.createTempDir(), Collections.EMPTY_LIST};

                URL[] urLs = ((URLClassLoader) Launch.class.getClassLoader()).getURLs();
                loader = new LaunchClassLoader(urLs);

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
}