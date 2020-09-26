package amata1219.redis.plugin.messages.common

import amata1219.redis.plugin.messages.common.config.{HierarchicalConfiguration, OverallConfiguration}
import io.lettuce.core.resource.DefaultClientResources
import io.lettuce.core.{ClientOptions, RedisClient, RedisURI}

object RedisClientCreation {

  def createClientBasedOn(config: OverallConfiguration[_, _]): RedisClient = {
    val redisSection: HierarchicalConfiguration[_] = config.section("redis-server")

    val resourcesSection: HierarchicalConfiguration[_] = redisSection.section("resources")
    val ioThreadPoolSize = resourcesSection.int("io-thread-pool-size")
    val computationThreadPoolSize = resourcesSection.int("computation-thread-pool-size")

    val resources: DefaultClientResources = DefaultClientResources.builder()
      .ioThreadPoolSize(ioThreadPoolSize)
      .computationThreadPoolSize(computationThreadPoolSize)
      .build()

    val options: ClientOptions = ClientOptions.builder()
      .autoReconnect(true)
      .build()

    val address: String = s"${redisSection.string("url")}:${redisSection.int("port")}"

    val uri: RedisURI = redisSection.string("password") match {
      case password if !password.isEmpty => RedisURI.create(s"redis://$password@$address")
      case _ => RedisURI.create(s"redis://$address")
    }

    val client: RedisClient = RedisClient.create(resources, uri)
    client.setOptions(options)

    client
  }

}
