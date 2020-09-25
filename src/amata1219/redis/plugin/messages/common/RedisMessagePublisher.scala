package amata1219.redis.plugin.messages.common

import amata1219.redis.plugin.messages.common.message.{Destination, Message}
import io.lettuce.core.api.StatefulRedisConnection

class RedisMessagePublisher(
  val connection: StatefulRedisConnection[String, String],
  val bungeeName: String,
  val serverName: String
){

  def publish(destinationServer: String, channel: String, message: String*): Long = {
    publish(new Destination(bungeeName, destinationServer), new Message(serverName, channel, message:_*))
  }

  def publish(destination: Destination, message: Message): Long = synchronized {
    connection.sync().publish(destination.toString, message.toString)
  }

}
