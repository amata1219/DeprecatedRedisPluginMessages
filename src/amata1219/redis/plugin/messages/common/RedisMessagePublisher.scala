package amata1219.redis.plugin.messages.common

import amata1219.redis.plugin.messages.common.message.Message
import io.lettuce.core.api.StatefulRedisConnection

class RedisMessagePublisher(val connection: StatefulRedisConnection[String, String], val serverName: String){

  def publish(destinationServerName: String, channel: String, message: List[String]): Long = {
    publish(destinationServerName, new Message(serverName, channel, message:_*))
  }

  def publish(destinationServerName: String, message: Message): Long = synchronized {
    connection.sync().publish(destinationServerName, message.toString)
  }

}