package amata1219.redis.plugin.messages.common

import io.lettuce.core.api.StatefulRedisConnection

class RedisMessagePublisher(
  val connection: StatefulRedisConnection[String, String],
  val bungeeName: String,
  val serverName: String
){

  def publish(destinationServer: String, channel: String, message: String*): Long = synchronized {
    import amata1219.redis.plugin.messages.common.MessagedDataSeparator._
    val header: String = s"$serverName$SEPARATOR$channel"
    val headeredData: String = message.fold(header)((m1, m2) => s"$m1$SEPARATOR$m2")
    connection.sync().publish(s"$bungeeName:$destinationServer", headeredData)
  }

}
