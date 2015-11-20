package foxie.serverlister.messages;

import net.minecraft.entity.player.EntityPlayer;

public class MessagePlayerJoined extends MessageBase {

   EntityPlayer player;

   public MessagePlayerJoined(EntityPlayer player) {
      this.player = player;
   }

   @Override
   public byte[] encode() {
      return ("uuid: " + player.getUniqueID()).getBytes();
   }

   @Override
   public String getURL() {
      return "player_joined";
   }
}
