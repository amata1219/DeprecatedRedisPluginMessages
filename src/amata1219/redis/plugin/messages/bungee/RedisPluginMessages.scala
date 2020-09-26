package amata1219.redis.plugin.messages.bungee

import java.util

import amata1219.redis.plugin.messages.bungee.config.Configuration
import amata1219.redis.plugin.messages.bungee.listener.RedisMessageReceivedListener
import amata1219.redis.plugin.messages.common.{RedisClientCreation, RedisMessagePublisher}
import amata1219.redis.plugin.messages.common.message.RedisChannel
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config

import scala.jdk.CollectionConverters._

class RedisPluginMessages extends Plugin with RedisPluginMessagesAPI {

  RedisPluginMessages.instance = this

  val configuration = new Configuration("config.yml")
  configuration.create()
  configuration.reload()

  var client: RedisClient = _
  var standaloneConnection: StatefulRedisConnection[String, String] = _
  var pubSubConnection: StatefulRedisPubSubConnection[String, String] = _
  var publisher: RedisMessagePublisher = _
  var listener: RedisMessageReceivedListener = _

  override def onEnable(): Unit = {
    client = RedisClientCreation.createClientBasedOn(configuration)

    standaloneConnection = client.connect()
    pubSubConnection = client.connectPubSub()

    val file: config.Configuration = configuration.config
    val linkedBungeeName: String = file.getString("linked-bungee-name")
    val serverNameInBungeeNetwork: String = file.getString("server-name-in-bungee-network")

    //指定した要素に基づいてpublishするインスタンスを作成する
    publisher = new RedisMessagePublisher(pubSubConnection, linkedBungeeName, RedisChannel.BUNGEE)

    //このサーバーがsubscribeするチャンネルを指定する
    val channelSubscribed = new RedisChannel(linkedBungeeName, RedisChannel.BUNGEE)
    pubSubConnection.sync().subscribe(channelSubscribed.toString)

    listener = new RedisMessageReceivedListener()
    pubSubConnection.addListener(listener)
  }

  override def onDisable(): Unit = {
    pubSubConnection.removeListener(listener)
    pubSubConnection.close()
    standaloneConnection.close()
    client.shutdown()
  }

  override def sendRedisPluginMessage(destinationServerName: String, channel: String, message: util.List[String]): Unit = {
    publisher.publish(destinationServerName, channel, message.asScala.toList)
  }

}
object RedisPluginMessages {

  var instance: RedisPluginMessages = _

}
