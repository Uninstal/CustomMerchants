package org.uninstal.npctraiders.data.reflection;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Field;
import org.bukkit.entity.Player;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class ReflectionUtils
{
    @SuppressWarnings("deprecation")
	public static Object createNMSGameMode(final GameMode gameMode) {
        Class<?> c = getClassByName(String.valueOf(getNMSPackageName()) + ".EnumGamemode");
        if (c == null) {
            c = getClassByName(String.valueOf(getNMSPackageName()) + ".WorldSettings$EnumGamemode");
        }
        try {
            final Method method = c.getDeclaredMethod("getById", Integer.TYPE);
            method.setAccessible(true);
            return method.invoke(null, gameMode.getValue());
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object createPlayerInfoData(final Object gameProfile, final GameMode gameMode, final int ping, final String nickName) {
    	
        Class<?> playerInfoDataClass = getClassByName(String.valueOf(getNMSPackageName()) + ".PacketPlayOutPlayerInfo$PlayerInfoData");
        if (playerInfoDataClass == null) {
            playerInfoDataClass = getClassByName(String.valueOf(getNMSPackageName()) + ".PlayerInfoData");
        }
        final Object nmsGameMode = createNMSGameMode(gameMode);
        try {
            final Constructor<?> constructor = playerInfoDataClass.getDeclaredConstructor(getClassByName(String.valueOf(getNMSPackageName()) + ".PacketPlayOutPlayerInfo"), getClassByName("com.mojang.authlib.GameProfile"), Integer.TYPE, nmsGameMode.getClass(), getClassByName(String.valueOf(getNMSPackageName()) + ".IChatBaseComponent"));
            constructor.setAccessible(true);
            return constructor.newInstance(null, gameProfile, ping, nmsGameMode, createNMSTextComponent(nickName));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object fillProfileProperties(final Object gameProfile) {
        final Class<?> serverClass = getClassByName(String.valueOf(getNMSPackageName()) + ".MinecraftServer");
        final Class<?> sessionServiceClass = getClassByName("com.mojang.authlib.minecraft.MinecraftSessionService");
        try {
            final Method method = serverClass.getDeclaredMethod("B_", (Class[])new Class[0]);
            method.setAccessible(true);
            final Object minecraftServer = method.invoke(null, new Object[0]);
            String methodName;
            if (existsMethod(serverClass, "aC", sessionServiceClass)) {
                methodName = "aC";
            }
            else {
                methodName = "aD";
            }
            Method method2 = serverClass.getDeclaredMethod(methodName, (Class[])new Class[0]);
            method2.setAccessible(true);
            final Object sessionService = method2.invoke(minecraftServer, new Object[0]);
            method2 = sessionServiceClass.getDeclaredMethod("fillProfileProperties", gameProfile.getClass(), Boolean.TYPE);
            method2.setAccessible(true);
            final Object result = method2.invoke(sessionService, gameProfile, true);
            return result;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static boolean existsMethod(final Class<?> clazz, final String methodName, final Class<?> returnClass) {
        Method[] declaredMethods;
        for (int length = (declaredMethods = clazz.getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (method.getName().equals(methodName) && method.getGenericReturnType() == returnClass) {
                return true;
            }
        }
        return false;
    }
    
    public static Object searchUUID(final String playerName) {
        final Class<?> serverClass = getClassByName(String.valueOf(getNMSPackageName()) + ".MinecraftServer");
        final Class<?> userCacheClass = getClassByName(String.valueOf(getNMSPackageName()) + ".UserCache");
        try {
            final Method method = serverClass.getDeclaredMethod("B_", (Class[])new Class[0]);
            method.setAccessible(true);
            final Object minecraftServer = method.invoke(null, new Object[0]);
            Method method2 = serverClass.getDeclaredMethod("getUserCache", (Class[])new Class[0]);
            method2.setAccessible(true);
            final Object userCache = method2.invoke(minecraftServer, new Object[0]);
            method2 = userCacheClass.getDeclaredMethod("getProfile", String.class);
            method2.setAccessible(true);
            return method2.invoke(userCache, playerName);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
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
    
    public static Object getField(final Class<?> c, final Object obj, final String key) throws Exception {
        final Field field = c.getDeclaredField(key);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    public static void replaceField(final Class<?> c, final Object obj, final String key, final Object value) throws Exception {
        final Field field = c.getDeclaredField(key);
        field.setAccessible(true);
        field.set(obj, value);
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
    
    public static class PlayerInfoAction
    {
        public static Object UPDATE_GAME_MODE;
        public static Object ADD_PLAYER;
        public static Object UPDATE_DISPLAY_NAME;
        public static Object REMOVE_PLAYER;
        private static Class<?> nmsClass;
        
        static {
            PlayerInfoAction.UPDATE_GAME_MODE = getNMSAction("UPDATE_GAME_MODE");
            PlayerInfoAction.ADD_PLAYER = getNMSAction("ADD_PLAYER");
            PlayerInfoAction.UPDATE_DISPLAY_NAME = getNMSAction("UPDATE_DISPLAY_NAME");
            PlayerInfoAction.REMOVE_PLAYER = getNMSAction("REMOVE_PLAYER");
        }
        
        private static Object getNMSAction(final String name) {
            try {
                final Field field = getNMSClass().getDeclaredField(name);
                field.setAccessible(true);
                return field.get(null);
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        public static Class<?> getNMSClass() {
            if (PlayerInfoAction.nmsClass == null) {
                PlayerInfoAction.nmsClass = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                if (PlayerInfoAction.nmsClass == null) {
                    PlayerInfoAction.nmsClass = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".EnumPlayerInfoAction");
                }
            }
            return PlayerInfoAction.nmsClass;
        }
    }
}
