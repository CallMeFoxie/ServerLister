package foxie.serverlister.messages;

import net.minecraft.server.MinecraftServer;

public class MessageMOTD extends MessageBase {
   @Override
   public byte[] encode() {
      return MinecraftServer.getServer().getMOTD().getBytes();
   }

   @Override
   public String getURL() {
      return "motd";
   }
}
