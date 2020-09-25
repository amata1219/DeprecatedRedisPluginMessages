package amata1219.redis.plugin.messages.common.message

class Destination(val bungeeName: String, val serverName: String) {

  override def toString: String = s"$bungeeName:$serverName"

}
