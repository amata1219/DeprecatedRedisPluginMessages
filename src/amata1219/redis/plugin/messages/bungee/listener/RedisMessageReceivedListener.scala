package amata1219.redis.plugin.messages.bungee.listener

import amata1219.redis.plugin.messages.bungee.RedisPluginMessages
import amata1219.redis.plugin.messages.bungee.event.RedisMessageReceivedEvent
import amata1219.redis.plugin.messages.common.RedisMessageReceiver

import scala.jdk.CollectionConverters._

class RedisMessageReceivedListener extends RedisMessageReceiver {

  override def receive(sourceServerName: String, channel: String, message: List[String]): Unit = {
    val event = new RedisMessageReceivedEvent(sourceServerName, channel, message.asJava)
    RedisPluginMessages.instance.getProxy.getPluginManager.callEvent(event)
  }

}
