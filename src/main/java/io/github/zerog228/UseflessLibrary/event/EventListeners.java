package io.github.zerog228.UseflessLibrary.event;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.github.zerog228.UseflessLibrary.structure.CStructureHelper;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;

public class EventListeners implements Listener {

    //Events related to custom structures
    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e){
        CStructureHelper.removeTag(e.getBlock());
    }

    @EventHandler
    public void blockDestroyEvent(BlockDestroyEvent e){
        CStructureHelper.removeTag(e.getBlock());
    }

    @EventHandler
    public void pistonExtendEvent(BlockPistonExtendEvent e){
        CStructureHelper.removeTag(e.getBlock().getRelative(e.getDirection()));
    }

    @EventHandler
    public void pistonRetractEvent(BlockPistonRetractEvent e){
        CStructureHelper.removeTag(e.getBlock().getRelative(e.getDirection()));
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.getItem() == null && e.getPlayer().isSneaking()){
            Block block = CStructureHelper.getMainBlock(e.getClickedBlock());
            if(block != null && block.getState() instanceof InventoryHolder holder){
                e.getPlayer().openInventory(holder.getInventory());
            }
        }
    }
}
