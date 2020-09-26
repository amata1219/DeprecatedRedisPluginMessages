package amata1219.redis.plugin.messages.common.config

trait HierarchicalConfiguration[T] {

  def get(path: String): Any

  def string(path: String): String = get(path).asInstanceOf[String]

  def int(path: String): Int = get(path).asInstanceOf[Number].intValue()

  def section(path: String): HierarchicalConfiguration[T]

}
