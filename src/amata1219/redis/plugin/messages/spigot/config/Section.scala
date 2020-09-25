package amata1219.redis.plugin.messages.spigot.config

import amata1219.redis.plugin.messages.common.config
import org.bukkit.configuration.ConfigurationSection

class Section(val section: ConfigurationSection) extends config.Configuration[ConfigurationSection] {

  override def string(path: String): String = section.getString(path)

  override def int(path: String): Int = section.getInt(path)

  override def section(path: String): config.Configuration[ConfigurationSection] = new Section(section.getConfigurationSection(path))

}
