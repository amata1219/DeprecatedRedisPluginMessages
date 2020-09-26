package amata1219.redis.plugin.messages.common.message

class RedisChannel(val bungeeName: String, val serverName: String) {

  override def toString: String = s"$bungeeName:$serverName"

}
object RedisChannel {

  val BUNGEE: String = "BungeeCord"

}
