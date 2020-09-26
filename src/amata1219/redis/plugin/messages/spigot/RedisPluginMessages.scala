package amata1219.redis.plugin.messages.spigot

import java.util

import amata1219.redis.plugin.messages.common.{Identifier, RedisClientCreation, RedisMessagePublisher}
import amata1219.redis.plugin.messages.spigot.config.ConfigurationFile
import amata1219.redis.plugin.messages.spigot.listener.RedisMessageReceivedListener
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import org.bukkit.plugin.java.JavaPlugin

import scala.jdk.CollectionConverters._

class RedisPluginMessages extends JavaPlugin() with RedisPluginMessagesAPI {

  RedisPluginMessages.instance = this

  val configuration = new ConfigurationFile("config.yml")
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

    val serverName: String = configuration.config.getString("universally-unique-server-name")

    //指定した要素に基づいてpublishするインスタンスを作成する
    publisher = new RedisMessagePublisher(pubSubConnection, serverName)

    //このサーバーがsubscribeするチャンネルを指定する
    pubSubConnection.sync().subscribe(serverName)

    listener = new RedisMessageReceivedListener()
    pubSubConnection.addListener(listener)
  }

  override def onDisable(): Unit = {
    pubSubConnection.removeListener(listener)
    pubSubConnection.close()
    standaloneConnection.close()
    client.shutdown()
    client.getResources.shutdown()
  }

  override def sendRedisPluginMessage(channel: String, message: util.List[String]): Unit = {
    publisher.publish(Identifier.BUNGEE_CORD, channel, message.asScala.toList)
  }

}
object RedisPluginMessages {

  var instance: RedisPluginMessages = _

}
