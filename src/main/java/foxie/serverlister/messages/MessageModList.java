package foxie.serverlister.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class MessageModList extends MessageBase {
   @Override
   public byte[] encode() {
      JsonObject data = new JsonObject();
      JsonArray array = new JsonArray();
      for (ModContainer modContainer : Loader.instance().getActiveModList()) {
         JsonObject object = new JsonObject();
         object.addProperty("modid", modContainer.getModId());
         object.addProperty("version", modContainer.getVersion());
         object.addProperty("name", modContainer.getName());
         //data.add(modContainer.getModId(), object);
         array.add(object);
      }
      data.add("mods", array);
      return data.toString().getBytes();
   }

   @Override
   public String getURL() {
      return "mods";
   }
}
