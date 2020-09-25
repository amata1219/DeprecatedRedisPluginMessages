package amata1219.redis.plugin.messages.common.config

trait Configuration[T] {

  def string(path: String): String

  def int(path: String): Int

  def section(path: String): Configuration[T]

}
