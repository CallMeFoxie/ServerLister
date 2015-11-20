package foxie.serverlister;

import foxie.lib.FoxLog;
import foxie.serverlister.messages.MessageBase;
import foxie.serverlister.messages.MessageServerAlive;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InformThread extends Thread {
   private static final String upstream = "http://localhost";
   private static final int    port     = 36937;

   private static final String uri = "/remote/?command=%s";

   private static volatile LinkedBlockingQueue<MessageBase> messages;
   private static volatile boolean keepRunning = true;

   private ScheduledExecutorService executorService;

   public InformThread() {

      messages = new LinkedBlockingQueue<MessageBase>();
      setName("Server Lister notification thread");

      executorService = Executors.newSingleThreadScheduledExecutor();
      executorService.scheduleAtFixedRate(new Runnable() {
         @Override
         public void run() {
            addMessage(new MessageServerAlive());
         }
      }, 0, 10, TimeUnit.SECONDS);
   }

   public static void addMessage(MessageBase base) {
      messages.add(base);
   }

   @Override
   public void run() {
      while (keepRunning) {
         while (!messages.isEmpty()) {
            try {
               sendMessage(messages.take());
            } catch (Exception e) {
               FoxLog.error("Error sending message to the upstream server!");
               e.printStackTrace();
            }
         }
         try {
            Thread.sleep(5000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   private void sendMessage(MessageBase base) throws IOException {
      HttpClient client = HttpClients.createDefault();
      HttpPost post = new HttpPost(upstream + ":" + port + String.format(uri, base.getURL()));
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("msg", Base64.encodeBase64String(base.encode())));

      post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

      HttpResponse response = client.execute(post);
      if (response.getStatusLine().getStatusCode() != 200) {
         FoxLog.error("Non-200 reply on message! " + response.getStatusLine().getStatusCode());
      }
   }

   public void prepareToStop() {
      keepRunning = false;
      executorService.shutdown();
   }

}
