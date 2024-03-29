package amata1219.redis.plugin.messages.common

import amata1219.redis.plugin.messages.common.exception.IllegalFormattedMessageException
import amata1219.redis.plugin.messages.common.message.MessageSeparator
import io.lettuce.core.pubsub.RedisPubSubListener

trait RedisMessageReceiver extends RedisPubSubListener[String, String] {

  override def message(channel: String, message: String): Unit = {
    val components: Array[String] = message.split(MessageSeparator.SEPARATOR)

    if (components.length < 3) throw new IllegalFormattedMessageException(s"Received illegal formatted redis message on $channel: $message")

    receive(components(0), components(1), components.drop(2).toList)
  }

  def receive(sourceServerName: String, channel: String, message: List[String]): Unit

  //以下のメソッドは使わない

  override def message(pattern: String, channel: String, message: String): Unit = ()

  override def subscribed(channel: String, count: Long): Unit = ()

  override def psubscribed(pattern: String, count: Long): Unit = ()

  override def unsubscribed(channel: String, count: Long): Unit = ()

  override def punsubscribed(pattern: String, count: Long): Unit = ()

}
