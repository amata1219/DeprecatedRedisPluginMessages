package amata1219.redis.plugin.messages.common

import io.lettuce.core.api.StatefulRedisConnection

class RedisMessageSender(val connection: StatefulRedisConnection[String, String]) {

}
