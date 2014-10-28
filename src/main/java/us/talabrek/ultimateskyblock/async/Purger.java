package us.talabrek.ultimateskyblock.async;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import us.talabrek.ultimateskyblock.UUIDPlayerInfo;
import us.talabrek.ultimateskyblock.uSkyBlock;

import java.io.File;
import java.util.LinkedList;

public class Purger implements Runnable {
    private long mEarliest;
    private boolean mNoIsland;

    public Purger(long time, boolean includeNoIsland) {
        mEarliest = System.currentTimeMillis() - time;
        mNoIsland = includeNoIsland;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        File directoryPlayers = uSkyBlock.getInstance().directoryPlayers;

        uSkyBlock.getLog().info("Preparing list of islands to purge.");

        LinkedList<UUIDPlayerInfo> toRemove = new LinkedList<UUIDPlayerInfo>();

        for (File child : directoryPlayers.listFiles()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(child.getName());
            if (player.hasPlayedBefore() && !player.isOnline()) {
                if (player.getLastPlayed() < mEarliest || (mNoIsland && !uSkyBlock.getInstance().hasIsland(player.getUniqueId()))) {
                    UUIDPlayerInfo pi = uSkyBlock.getInstance().getPlayerNoStore(player.getUniqueId());
                    if (pi != null && (mNoIsland || pi.getHasIsland()))
                        toRemove.add(pi);
                }
            }
        }

        IslandRemover remover = new IslandRemover(toRemove);
        remover.start();
    }

}
