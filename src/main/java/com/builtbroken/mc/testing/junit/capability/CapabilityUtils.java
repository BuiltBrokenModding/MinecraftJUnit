package com.builtbroken.mc.testing.junit.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CapabilityUtils {
    @SuppressWarnings("unchecked")
    public static <T> Capability<T> getCapabilityForClass(Class<T> capClass) {
        try {
            Field capabilityMap = CapabilityManager.class.getDeclaredField("providers");
            capabilityMap.setAccessible(true);
            Object instanceMap = capabilityMap.get(CapabilityManager.INSTANCE);
            if (instanceMap instanceof Map) {
                return (Capability<T>) ((Map) instanceMap).get(capClass.getName().intern());
            }
        } catch (NoSuchFieldException|IllegalAccessException e) {
            throw new IllegalStateException("Didn't get Capability Providers");
        }

        throw new IllegalStateException("Didn't get Capability for Class");
    }

    public static <T> void setupCapability(Class<T> capClass, Consumer<Void> registerCall, Consumer<Capability<T>> settingCapField) {
        registerCall.accept(null);
        settingCapField.accept(getCapabilityForClass(capClass));
    }
}
