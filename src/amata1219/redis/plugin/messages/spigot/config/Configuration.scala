package amata1219.redis.plugin.messages.spigot.config

import java.io.{File, IOException, InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import amata1219.redis.plugin.messages.common.config.{HierarchicalConfiguration, OverallConfiguration}
import amata1219.redis.plugin.messages.spigot.RedisPluginMessages
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}

class Configuration(fileName: String) extends OverallConfiguration[FileConfiguration, ConfigurationSection](fileName) {

  private val plugin: RedisPluginMessages = RedisPluginMessages.instance
  private val file: File = new File(plugin.getDataFolder, fileName)

  override def create(): Unit = if (!file.exists()) {
    plugin.saveResource(fileName, false)
  }

  override def reload(): Unit = {
    config = YamlConfiguration.loadConfiguration(file)

    val stream: InputStream = plugin.getResource(fileName)
    if (stream != null) {
      val reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
      val default: YamlConfiguration = YamlConfiguration.loadConfiguration(reader)
      config.setDefaults(default)
    }
  }

  override def save(): Unit = {
    if (config != null) {
      try config.save(file)
      catch {
        case ex: IOException =>
          println(s"Could not save config to $fileName")
          ex.printStackTrace()
      }
    }
  }

  override def get(path: String): Any = config.get(path)

  override def section(path: String): HierarchicalConfiguration[ConfigurationSection] = new ConfigurationHierarchy(config.getConfigurationSection(path))

}
