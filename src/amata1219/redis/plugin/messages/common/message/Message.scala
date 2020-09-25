package amata1219.redis.plugin.messages.common.message

class Message(val sourceServerName: String, val channel: String, val message: String*) {

  override def toString: String = {
    import MessageSeparator._

    val header: String = s"$sourceServerName$SEPARATOR$channel"
    message.fold(header)((m1, m2) => s"$m1$SEPARATOR$m2")
  }

}
