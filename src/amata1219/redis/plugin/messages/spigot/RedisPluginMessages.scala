package amata1219.redis.plugin.messages.spigot

import java.util

import amata1219.redis.plugin.messages.common.message.RedisChannel
import amata1219.redis.plugin.messages.common.{RedisClientCreation, RedisMessagePublisher}
import amata1219.redis.plugin.messages.spigot.config.Configuration
import amata1219.redis.plugin.messages.spigot.listener.RedisMessageReceivedListener
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

import scala.jdk.CollectionConverters._

class RedisPluginMessages extends JavaPlugin() with RedisPluginMessagesAPI {

  RedisPluginMessages.instance = this

  val config = new Configuration("config.yml")
  config.create()

  var standaloneConnection: StatefulRedisConnection[String, String] = _
  var pubSubConnection: StatefulRedisPubSubConnection[String, String] = _
  var publisher: RedisMessagePublisher = _
  var listener: RedisMessageReceivedListener = _

  override def onEnable(): Unit = {
    val client: RedisClient = RedisClientCreation.createClientBasedOn(config)

    standaloneConnection = client.connect()
    pubSubConnection = client.connectPubSub()

    val file: FileConfiguration = config.config
    val linkedBungeeName: String = file.getString("linked-bungee-name")
    val serverNameInBungeeNetwork: String = file.getString("server-name-in-bungee-network")

    //指定した要素に基づいてpublishするインスタンスを作成する
    publisher = new RedisMessagePublisher(pubSubConnection, linkedBungeeName, serverNameInBungeeNetwork)

    //このサーバーがsubscribeするチャンネルを指定する
    val channelSubscribed = new RedisChannel(linkedBungeeName, serverNameInBungeeNetwork)
    pubSubConnection.sync().subscribe(channelSubscribed.toString)

    listener = new RedisMessageReceivedListener()
    pubSubConnection.addListener(listener)
  }

  override def onDisable(): Unit = {
    pubSubConnection.removeListener(listener)
    pubSubConnection.close()
    standaloneConnection.close()
  }

  override def sendRedisPluginMessage(channel: String, message: util.List[String]): Unit = {
    publisher.publish("BungeeCord", channel, message.asScala.toList)
  }

}
object RedisPluginMessages {

  var instance: RedisPluginMessages = _

}
