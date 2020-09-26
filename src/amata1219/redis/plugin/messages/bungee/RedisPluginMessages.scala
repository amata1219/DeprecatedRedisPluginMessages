package amata1219.redis.plugin.messages.bungee

import java.util

import amata1219.redis.plugin.messages.bungee.config.ConfigurationFile
import amata1219.redis.plugin.messages.bungee.listener.RedisMessageReceivedListener
import amata1219.redis.plugin.messages.common.{Identifier, RedisClientCreation, RedisMessagePublisher}
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import net.md_5.bungee.api.plugin.Plugin

import scala.jdk.CollectionConverters._

class RedisPluginMessages extends Plugin with RedisPluginMessagesAPI {

  RedisPluginMessages.instance = this

  val configuration = new ConfigurationFile("config.yml")
  configuration.create()
  configuration.reload()

  var client: RedisClient = _
  var standaloneConnection: StatefulRedisConnection[String, String] = _
  var connectionToSubscribe: StatefulRedisPubSubConnection[String, String] = _
  var publisher: RedisMessagePublisher = _
  var listener: RedisMessageReceivedListener = _

  override def onEnable(): Unit = {
    client = RedisClientCreation.createClientBasedOn(configuration)

    standaloneConnection = client.connect()
    connectionToSubscribe = client.connectPubSub()

    //指定した要素に基づいてpublishするインスタンスを作成する
    publisher = new RedisMessagePublisher(standaloneConnection, Identifier.BUNGEE_CORD)

    //このサーバーがsubscribeするチャンネルを指定する
    connectionToSubscribe.sync().subscribe(Identifier.BUNGEE_CORD)

    listener = new RedisMessageReceivedListener()
    connectionToSubscribe.addListener(listener)
  }

  override def onDisable(): Unit = {
    connectionToSubscribe.removeListener(listener)
    connectionToSubscribe.close()
    standaloneConnection.close()
    client.shutdown()
    client.getResources.shutdown()
  }

  override def sendRedisPluginMessage(destinationServerName: String, channel: String, message: util.List[String]): Unit = {
    publisher.publish(destinationServerName, channel, message.asScala.toList)
  }

}
object RedisPluginMessages {

  var instance: RedisPluginMessages = _

}
