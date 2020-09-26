package amata1219.redis.plugin.messages.spigot.config

import amata1219.redis.plugin.messages.common.config.HierarchicalConfiguration
import org.bukkit.configuration.ConfigurationSection

class ConfigurationHierarchy(val hierarchy: ConfigurationSection) extends HierarchicalConfiguration[ConfigurationSection] {

  override def get(path: String): Any = hierarchy.get(path)

  override def section(path: String): HierarchicalConfiguration[ConfigurationSection] = new ConfigurationHierarchy(hierarchy.getConfigurationSection(path))

}
