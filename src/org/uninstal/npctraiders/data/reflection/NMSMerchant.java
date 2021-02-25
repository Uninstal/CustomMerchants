package org.uninstal.npctraiders.data.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public class NMSMerchant implements InvocationHandler
{
    private ReflectionUtils.NMSMerchantRecipeList o;
    private transient Object c;
    public Object proxy;
    public String title;
    
    public NMSMerchant() {
        this.o = new ReflectionUtils.NMSMerchantRecipeList();
    }
    
    @Override
    public Object invoke(final Object proxy, final Method m, final Object[] args) {
        try {
            if (m == null || m.getName() == null) {
                return null;
            }
            final Class<?> entityHuman = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".EntityHuman");
            if (m.getName().equals("setTradingPlayer") && args.length == 1 && args[0] != null && args[0].getClass().isInstance(entityHuman)) {
                this.setTradingPlayer(args[0]);
            }
            else {
                if (m.getName().equals("b") || m.getName().equals("m_") || m.getName().equals("getTrader") || m.getName().equals("v_")) {
                    return this.getEntityHuman();
                }
                if (m.getName().equals("getOffers") && args.length == 1) {
                    return this.getOffers(args[0]);
                }
                if (m.getName().equals("getScoreboardDisplayName")) {
                    return this.getScoreboardDisplayName();
                }
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public Object getScoreboardDisplayName() {
        return ReflectionUtils.createNMSTextComponent(this.title);
    }
    
    public void setTradingPlayer(final Object player) {
        this.c = player;
    }
    
    public Object getEntityHuman() {
        return this.c;
    }
    
    public Object getOffers(final Object player) {
        return this.o.getHandle();
    }
    
    public void a(final Object recipe) {
        this.o.add(new ReflectionUtils.NMSMerchantRecipe(recipe));
    }
    
    public Player getBukkitEntity() {
        try {
            final Class<?> c = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".EntityHuman");
            final Method m = c.getDeclaredMethod("getBukkitEntity", (Class[])new Class[0]);
            m.setAccessible(true);
            return (Player)m.invoke(this.c, new Object[0]);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public void clearRecipes() {
        this.o.clear();
    }
    
    public void setRecipes(final ReflectionUtils.NMSMerchantRecipeList recipes) {
        this.o = recipes;
    }
    
    public void openTrading(final Object player) {
        this.c = player;
        try {
            final Class<?> classs = ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".EntityPlayer");
            if (this.getMethodArgs(classs, "openTrade") == 2) {
                final Method m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".IMerchant"), String.class);
                m.setAccessible(true);
                m.invoke(player, this.proxy, this.title);
            }
            else {
                final Method m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".IMerchant"));
                m.setAccessible(true);
                m.invoke(player, this.proxy);
            }
        }
        catch (Exception ex) {}
    }
    
    private int getMethodArgs(final Class<?> classs, final String methodName) {
        Method[] declaredMethods;
        for (int length = (declaredMethods = classs.getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (method.getName().equals(methodName)) {
                return method.getParameterTypes().length;
            }
        }
        return -1;
    }
}
