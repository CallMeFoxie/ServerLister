package foxie.serverlister.messages;

import net.minecraft.entity.player.EntityPlayer;

public class MessagePlayerLeft extends MessagePlayerJoined {
   public MessagePlayerLeft(EntityPlayer player) {
      super(player);
   }

   @Override
   public String getURL() {
      return "player_left";
   }
}
