package foxie.serverlister.messages;

public class MessageServerAlive extends MessageBase {
   @Override
   public byte[] encode() {
      return new byte[0];
   }

   @Override
   public String getURL() {
      return "alive";
   }
}
