package amata1219.redis.plugin.messages.spigot

import java.io.{File, IOException, InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}

class Configuration(val fileName: String) {

  private val plugin: RedisPluginMessages = RedisPluginMessages.instance
  private val file: File = new File(plugin.getDataFolder, fileName)
  private var value: FileConfiguration = _

  def create(): Unit = if (!file.exists()) {
    plugin.saveResource(fileName, false)
  }

  def reload(): Unit = {
    value = YamlConfiguration.loadConfiguration(file)

    val stream: InputStream = plugin.getResource(fileName)
    if (stream != null) {
      val reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
      val default: YamlConfiguration = YamlConfiguration.loadConfiguration(reader)
      value.setDefaults(default)
    }
  }

  def save(): Unit = {
    if (value == null) return

    try {
      value.save(file)
    } catch {
      case ex: IOException =>
        println(s"Could not save config to $fileName")
        ex.printStackTrace()
    }
  }

  def config: FileConfiguration = {
    if (value == null) reload()
    value
  }

}
