package amata1219.redis.plugin.messages.spigot.listener

import amata1219.redis.plugin.messages.common.RedisMessageReceiver
import amata1219.redis.plugin.messages.spigot.event.RedisMessageReceivedEvent
import org.bukkit.Bukkit

class RedisMessageReceivedListener extends RedisMessageReceiver {

  override def receive(sourceServerName: String, channel: String, message: List[String]): Unit = {
    val event = new RedisMessageReceivedEvent(sourceServerName, channel, message)
    Bukkit.getServer.getPluginManager.callEvent(event)
  }

}
