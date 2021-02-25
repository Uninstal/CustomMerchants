package org.uninstal.npctraiders.data.reflection;

import org.bukkit.inventory.ItemStack;

public class MerchantOffer
{
    private ItemStack[] items;
    private int maxUses;
    
    public MerchantOffer(final ItemStack is1, final ItemStack is2, final ItemStack re) {
        this.items = new ItemStack[3];
        this.maxUses = Integer.MAX_VALUE;
        this.items[0] = is1;
        this.items[1] = is2;
        this.items[2] = re;
    }
    
    public MerchantOffer(final ItemStack is, final ItemStack re) {
        this(is, null, re);
    }
    
    protected MerchantOffer(final ReflectionUtils.NMSMerchantRecipe handle) {
        this.items = new ItemStack[3];
        this.maxUses = Integer.MAX_VALUE;
        this.items[0] = ReflectionUtils.OBCCraftItemStack.asBukkitCopy(handle.getBuyItem1());
        this.items[1] = ((handle.getBuyItem2() == null) ? null : ReflectionUtils.OBCCraftItemStack.asBukkitCopy(handle.getBuyItem2()));
        this.items[2] = ReflectionUtils.OBCCraftItemStack.asBukkitCopy(handle.getBuyItem3());
    }
    
    protected ReflectionUtils.NMSMerchantRecipe getHandle() {
        ReflectionUtils.NMSMerchantRecipe nms;
        if (this.items[1] == null) {
            nms = new ReflectionUtils.NMSMerchantRecipe(ReflectionUtils.OBCCraftItemStack.asNMSCopy(this.items[0]), ReflectionUtils.OBCCraftItemStack.asNMSCopy(this.items[2]));
        }
        else {
            nms = new ReflectionUtils.NMSMerchantRecipe(ReflectionUtils.OBCCraftItemStack.asNMSCopy(this.items[0]), ReflectionUtils.OBCCraftItemStack.asNMSCopy(this.items[1]), ReflectionUtils.OBCCraftItemStack.asNMSCopy(this.items[2]));
        }
        nms.setMaxUses(this.maxUses);
        return nms;
    }
    
    public ItemStack getFirstInput() {
        return this.items[0];
    }
    
    public ItemStack getSecondInput() {
        return this.items[1];
    }
    
    public ItemStack getOutput() {
        return this.items[2];
    }
    
    public int getMaxUses() {
        return this.maxUses;
    }
    
    public void setMaxUses(final int maxUses) {
        this.maxUses = maxUses;
    }
    
    public MerchantOffer clone() {
        final MerchantOffer clone = new MerchantOffer(this.getFirstInput(), this.getSecondInput(), this.getOutput());
        clone.setMaxUses(this.getMaxUses());
        return clone;
    }
}
