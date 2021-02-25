// 
// Decompiled by Procyon v0.5.36
// 

package org.uninstal.npctraiders.data.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Proxy;
import org.bukkit.Bukkit;

public class Merchant
{
    private NMSMerchant h;
    
    public Merchant() {
        this.h = new NMSMerchant();
        this.h.proxy = Proxy.newProxyInstance(Bukkit.class.getClassLoader(), new Class[] { ReflectionUtils.getClassByName(String.valueOf(ReflectionUtils.getNMSPackageName()) + ".IMerchant") }, this.h);
    }
    
    public String getTitle() {
        return this.h.title;
    }
    
    public void setTitle(final String title) {
        this.h.title = title;
    }
    
    public Merchant addOffer(final MerchantOffer offer) {
        this.h.a(offer.getHandle().getMerchantRecipe());
        return this;
    }
    
    public Player getCustomer() {
        return (this.h.getEntityHuman() == null) ? null : this.h.getBukkitEntity();
    }
    
    public Merchant setCustomer(final Player player) {
        this.h.setTradingPlayer((player == null) ? null : ReflectionUtils.toEntityHuman(player));
        return this;
    }
    
    public void openTrading(final Player player) {
        this.h.openTrading(ReflectionUtils.toEntityHuman(player));
    }
}
