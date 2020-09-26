package amata1219.redis.plugin.messages.bungee.config

import amata1219.redis.plugin.messages.common.config.HierarchicalConfiguration
import net.md_5.bungee.config

class ConfigurationHierarchy(val hierarchy: config.Configuration) extends HierarchicalConfiguration[config.Configuration] {

  override def get(path: String): Any = hierarchy.get(path)

  override def section(path: String): HierarchicalConfiguration[config.Configuration] = new ConfigurationHierarchy(hierarchy.getSection(path))

}
