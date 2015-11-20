package foxie.serverlister.messages;

public class MessageNOP extends MessageBase {
   @Override
   public byte[] encode() {
      return new byte[]{};
   }

   @Override
   public String getURL() {
      return "nop";
   }
}
