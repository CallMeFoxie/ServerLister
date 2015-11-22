package foxie.serverlister;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import foxie.lib.Config;
import foxie.lib.IFoxieMod;
import foxie.serverlister.messages.MessageMOTD;
import foxie.serverlister.messages.MessageModList;
import foxie.serverlister.messages.MessagePlayerJoined;
import foxie.serverlister.messages.MessagePlayerLeft;

@Mod(modid = ServerLister.MODID, name = ServerLister.NAME, version = ServerLister.VERSION, dependencies = "required-after:FoxieLib")
public class ServerLister implements IFoxieMod {
   public static final String MODID   = "serverlister";
   public static final String NAME    = "ServerLister";
   public static final String VERSION = "@VERSION@";
   public static Config config;
   public static InformThread informThread;
   @Mod.Instance(MODID)
   public ServerLister INSTANCE;

   public ServerLister() {
   }

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      config = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      informThread = new InformThread(event.getSuggestedConfigurationFile().getPath());
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
   }

   @Mod.EventHandler
   public void postinit(FMLPostInitializationEvent event) {
   }

   @Override
   public Config getConfig() {
      return config;
   }

   @Mod.EventHandler
   public void serverStarted(FMLServerStartedEvent event) {
      informThread.start();

      InformThread.addMessage(new MessageMOTD());
      InformThread.addMessage(new MessageModList());
   }

   @Mod.EventHandler
   public void serverClosing(FMLServerStoppingEvent event) {
      informThread.prepareToStop();
   }

   @SubscribeEvent
   public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
      InformThread.addMessage(new MessagePlayerJoined(event.player));
   }

   @SubscribeEvent
   public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      InformThread.addMessage(new MessagePlayerLeft(event.player));
   }
}

