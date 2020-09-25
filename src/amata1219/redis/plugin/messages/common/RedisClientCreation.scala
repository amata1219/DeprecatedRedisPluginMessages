package amata1219.redis.plugin.messages.common

import amata1219.redis.plugin.messages.spigot.Configuration
import io.lettuce.core.resource.DefaultClientResources
import io.lettuce.core.{ClientOptions, RedisClient, RedisURI}
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

object RedisClientCreation {

  def createClientBasedOn(config: Configuration): RedisClient = {
    val file: FileConfiguration = config.config
    val redisSection: ConfigurationSection = file.getConfigurationSection("redis-server")

    val resourcesSection: ConfigurationSection = redisSection.getConfigurationSection("resources")
    val ioThreadPoolSize = resourcesSection.getInt("io-thread-pool-size")
    val computationThreadPoolSize = resourcesSection.getInt("computation-thread-pool-size")

    val resources: DefaultClientResources = DefaultClientResources.builder()
      .ioThreadPoolSize(ioThreadPoolSize)
      .computationThreadPoolSize(computationThreadPoolSize)
      .build()

    val options: ClientOptions = ClientOptions.builder()
      .autoReconnect(true)
      .build()

    val address: String = s"${redisSection.getString("url")}:${redisSection.getString("port")}"

    val uri: RedisURI = redisSection.getString("password") match {
      case password if !password.isEmpty => RedisURI.create(s"redis://$password@$address")
      case _ => RedisURI.create(s"redis://$address")
    }

    val client: RedisClient = RedisClient.create(resources, uri)
    client.setOptions(options)

    client
  }

}
