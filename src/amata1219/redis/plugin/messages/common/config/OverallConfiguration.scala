package amata1219.redis.plugin.messages.common.config

abstract class OverallConfiguration[O, H](val fileName: String) extends HierarchicalConfiguration[H] {

  var config: O = _

  def create(): Unit

  def save(): Unit

  def reload(): Unit

}
