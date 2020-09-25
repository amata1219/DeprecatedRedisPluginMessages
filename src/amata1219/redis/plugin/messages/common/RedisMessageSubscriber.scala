package amata1219.redis.plugin.messages.common

import java.util.IllegalFormatException

import io.lettuce.core.pubsub.RedisPubSubListener

trait RedisMessageSubscriber[T] extends RedisPubSubListener[String, String] {

  override def message(channel: String, message: String): Unit = {
    val components: Array[String] = message.split(MessagedDataSeparator.SEPARATOR)

    if (components.length < 3) throw new IllegalFormatException(s"Received illegal formatted redis message on $channel: $message")

    subscribe(components(0), components(1), components.drop(2).toList)
  }

  def subscribe(sourceServerName: String, channel: String, data: List[String]): Unit

  override def message(pattern: String, channel: String, message: String): Unit = ()

  override def subscribed(channel: String, count: Long): Unit = ()

  override def psubscribed(pattern: String, count: Long): Unit = ()

  override def unsubscribed(channel: String, count: Long): Unit = ()

  override def punsubscribed(pattern: String, count: Long): Unit = ()

}
