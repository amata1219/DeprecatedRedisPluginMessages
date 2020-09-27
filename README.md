# RedisPluginMessages
　インメモリデータベースのRedisを利用してプレイヤーを介さずにPluginMessagingを行うSpigot/BungeeCordプラグイン。  
　従来のPluginMessagesとほぼ変わらない感覚でメッセージの送受信が出来ます。

## 同梱ライブラリ
- [lettuce-core 5.3.4 API adjusted for Spigot](https://github.com/amata1219/lettuce-core/releases/tag/5.3.4.RELEASE)

## 導入方法
1.Redisを導入し利用可能な状態にします。  
2.対象となるBungeeCord/SpigotのpluginsフォルダにRedisPluginMessagesフォルダを作成します。  
3.作成したRedisPluginMessagesフォルダに下記の内容のconfig.ymlを作成します。
```yaml
universally-unique-server-name: 'server'
#これはSpigot専用の設定項目であり、BungeeCordでは不要です。
#BungeeCordのネットワーク内で一意なサーバー名を設定して下さい。

redis-server:
#こちらはBungeeCord/Spigot共通の設定項目です。
#Redisサーバーに関する設定を行います。

  url: localhost
  port: 6379
  password: ''
  #パスワードがない場合は''(シングルクオーテーションを2個)と記述して下さい。

  resources:
    io-thread-pool-size: 4
    computation-thread-pool-size: 4
```  
4.各BungeeCord/SpigotのpluginsフォルダにRedisPluginMessages.jarを入れます。  
5.Redisサーバーを起動します。  
6.BungeeCord/Spigotを立ち上げると、PluginMessagingが利用可能な状態になります。


## サンプルコード
Spigot側
```Java
import amata1219.redis.plugin.messages.spigot.RedisPluginMessagesAPI;
import amata1219.redis.plugin.messages.spigot.event.RedisMessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class SpigotSemaphore implements Listener {

    @EventHandler
    public void on(RedisMessageReceivedEvent event) {
        if (!event.channel.equals("SpigotSemaphore")) return;
        
        Bukkit.broadcastMessage(event.sourceServerName + "からメッセージを受信しました！");
        event.message.forEach(Bukkit::broadcastMessage);
        
        Plugin maybeRedisPluginMessages = Bukkit.getPluginManager().getPlugin("RedisPluginMessages");
        if (!(maybeRedisPluginMessages instanceof RedisPluginMessagesAPI)) return;
        
        RedisPluginMessagesAPI api = (RedisPluginMessagesAPI) maybeRedisPluginMessages;
        api.sendRedisPluginMessage("BungeeSemaphore", "MESSAGE1", "MESSAGE2");
    }

}
```  
BungeeCord側
```Java
package amata1219.redis.plugin.messages;

import amata1219.redis.plugin.messages.bungee.RedisPluginMessagesAPI;
import amata1219.redis.plugin.messages.bungee.event.RedisMessageReceivedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeSemaphore implements Listener {

    @EventHandler
    public void on(RedisMessageReceivedEvent event) {
        if (!event.channel.equals("BungeeSemaphore")) return;

        ProxyServer proxy = Main.instance().getProxy();
        proxy.getLogger().info(event.sourceServerName + "からメッセージを受信しました！");
        event.message.forEach(proxy.getLogger()::info);

        Plugin maybeRedisPluginMessages = proxy.getPluginManager().getPlugin("RedisPluginMessages");
        if (!(maybeRedisPluginMessages instanceof RedisPluginMessagesAPI)) return;

        RedisPluginMessagesAPI api = (RedisPluginMessagesAPI) maybeRedisPluginMessages;
        api.sendRedisPluginMessage("lobby-server", "SpigotSemaphore", "MESSAGE1", "MESSAGE2");
    }

}

```