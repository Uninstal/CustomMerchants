package org.uninstal.npctraiders.data.reflection;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Field;
import org.bukkit.entity.Player;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class ReflectionUtils
{
    
    public static Object createNMSTextComponent(final String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final Class<?> c = getClassByName(String.valueOf(getNMSPackageName()) + ".ChatComponentText");
        try {
            final Constructor<?> constructor = c.getDeclaredConstructor(String.class);
            return constructor.newInstance(text);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object toEntityHuman(final Player player) {
        try {
            final Class<?> c = getClassByName(String.valueOf(getOBCPackageName()) + ".entity.CraftPlayer");
            final Method m = c.getDeclaredMethod("getHandle", (Class[])new Class[0]);
            m.setAccessible(true);
            return m.invoke(player, new Object[0]);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Class<?> getClassByName(final String name) {
        try {
            return Class.forName(name);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static String getNMSPackageName() {
        return "net.minecraft.server." + serverVersion();
    }
    
    public static String getOBCPackageName() {
        return "org.bukkit.craftbukkit." + serverVersion();
    }
    
    public static String serverVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
    
    public static class OBCCraftItemStack
    {
        public static Class<?> getOBCClass() {
            return ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getOBCPackageName()) + ".inventory.CraftItemStack");
        }
        
        public static ItemStack asBukkitCopy(final Object nmsItemStack) {
            try {
                final Method m = getOBCClass().getDeclaredMethod("asBukkitCopy", ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".ItemStack"));
                m.setAccessible(true);
                return (ItemStack)m.invoke(null, nmsItemStack);
            }
            catch (Exception e) {
                return null;
            }
        }
        
        public static Object asNMSCopy(final ItemStack stack) {
            try {
                final Method m = getOBCClass().getDeclaredMethod("asNMSCopy", ItemStack.class);
                m.setAccessible(true);
                return m.invoke(null, stack);
            }
            catch (Exception e) {
                return null;
            }
        }
    }
    
    public static class NMSMerchantRecipeList
    {
        private Object handle;
        
        public static Class<?> getNMSClass() {
            return ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".MerchantRecipeList");
        }
        
        public NMSMerchantRecipeList() {
            try {
                this.handle = getNMSClass().newInstance();
            }
            catch (Exception ex) {}
        }
        
        public NMSMerchantRecipeList(final Object handle) {
            this.handle = handle;
        }
        
        public Object getHandle() {
            return this.handle;
        }
        
        public void clear() {
            try {
                final Method m = ArrayList.class.getDeclaredMethod("clear", (Class<?>[])new Class[0]);
                m.setAccessible(true);
                m.invoke(this.handle, new Object[0]);
            }
            catch (Exception ex) {}
        }
        
        public void add(final NMSMerchantRecipe recipe) {
            try {
                final Method m = ArrayList.class.getDeclaredMethod("add", Object.class);
                m.setAccessible(true);
                m.invoke(this.handle, recipe.getMerchantRecipe());
            }
            catch (Exception ex) {}
        }
    }
    
    public static class NMSMerchantRecipe
    {
        private Object merchantRecipe;
        
        public NMSMerchantRecipe(final Object merchantRecipe) {
            this.merchantRecipe = merchantRecipe;
        }
        
        public NMSMerchantRecipe(final Object item1, final Object item3) {
            this(item1, null, item3);
        }
        
        public NMSMerchantRecipe(final Object item1, final Object item2, final Object item3) {
            try {
                final Class<?> isClass = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".ItemStack");
                this.merchantRecipe = getNMSClass().getDeclaredConstructor(isClass, isClass, isClass).newInstance(item1, item2, item3);
            }
            catch (Exception ex) {}
        }
        
        public static Class<?> getNMSClass() {
            return ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".MerchantRecipe");
        }
        
        public Object getBuyItem1() {
            try {
                final Method m = getNMSClass().getDeclaredMethod("getBuyItem1", (Class[])new Class[0]);
                m.setAccessible(true);
                return m.invoke(this.merchantRecipe, new Object[0]);
            }
            catch (Exception e) {
                return null;
            }
        }
        
        public Object getBuyItem2() {
            try {
                final Method m = getNMSClass().getDeclaredMethod("getBuyItem2", (Class[])new Class[0]);
                m.setAccessible(true);
                return m.invoke(this.merchantRecipe, new Object[0]);
            }
            catch (Exception e) {
                return null;
            }
        }
        
        public Object getBuyItem3() {
            try {
                final Method m = getNMSClass().getDeclaredMethod("getBuyItem3", (Class[])new Class[0]);
                m.setAccessible(true);
                return m.invoke(this.merchantRecipe, new Object[0]);
            }
            catch (Exception e) {
                return null;
            }
        }
        
        public int getMaxUses() {
            try {
                final Field field = getNMSClass().getDeclaredField("maxUses");
                field.setAccessible(true);
                return field.getByte(this.merchantRecipe);
            }
            catch (Exception e) {
                return 0;
            }
        }
        
        public void setMaxUses(final int maxUses) {
            try {
                final Field field = getNMSClass().getDeclaredField("maxUses");
                field.setAccessible(true);
                field.set(this.merchantRecipe, maxUses);
            }
            catch (Exception ex) {}
        }
        
        public Object getMerchantRecipe() {
            return this.merchantRecipe;
        }
    }
}
