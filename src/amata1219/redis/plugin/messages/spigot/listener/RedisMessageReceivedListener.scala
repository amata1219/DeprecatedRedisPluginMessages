package amata1219.redis.plugin.messages.spigot.listener

import amata1219.redis.plugin.messages.common.RedisMessageReceiver
import amata1219.redis.plugin.messages.spigot.RedisPluginMessages
import amata1219.redis.plugin.messages.spigot.event.RedisMessageReceivedEvent

import scala.jdk.CollectionConverters._

class RedisMessageReceivedListener extends RedisMessageReceiver {

  override def receive(sourceServerName: String, channel: String, message: List[String]): Unit = {
    val event = new RedisMessageReceivedEvent(sourceServerName, channel, message.asJava)
    RedisPluginMessages.instance.getServer.getPluginManager.callEvent(event)
  }

}
