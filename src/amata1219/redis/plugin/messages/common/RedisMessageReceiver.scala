package amata1219.redis.plugin.messages.common

import io.lettuce.core.pubsub.RedisPubSubListener

class RedisMessageReceiver[T](messageEventFiring: (String, String, List[String]) => T) extends RedisPubSubListener[String, String] {

  override def message(channel: String, data: String): Unit = {
    val components: Array[String] = data.split(RedisDataSpacer.SPACER)

    if (components.length < 3) {
      println(s"Received invalid formatted message: $data")
      return
    }

    messageEventFiring(components(1), components(2), components.drop(2).toList)
  }

  override def message(pattern: String, channel: String, data: String): Unit = ()

  override def subscribed(channel: String, count: Long): Unit = ()

  override def psubscribed(pattern: String, count: Long): Unit = ()

  override def unsubscribed(channel: String, count: Long): Unit = ()

  override def punsubscribed(pattern: String, count: Long): Unit = ()

}
