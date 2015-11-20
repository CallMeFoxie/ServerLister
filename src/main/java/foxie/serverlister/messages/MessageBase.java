package foxie.serverlister.messages;

public abstract class MessageBase {
   public abstract byte[] encode();

   public abstract String getURL();
}
