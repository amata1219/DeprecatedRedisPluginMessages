package amata1219.redis.plugin.messages.bungee.config

import java.io._

import amata1219.redis.plugin.messages.bungee.RedisPluginMessages
import amata1219.redis.plugin.messages.common.config.{HierarchicalConfiguration, OverallConfiguration}
import net.md_5.bungee.config.{Configuration, ConfigurationProvider, YamlConfiguration}

import scala.io.Source

class ConfigurationFile(fileName: String) extends OverallConfiguration[Configuration, Configuration](fileName) {

  private val plugin: RedisPluginMessages = RedisPluginMessages.instance
  private val file: File = new File(plugin.getDataFolder, fileName)

  implicit class XAutoCloseable[T <: AutoCloseable](resource: T) {
    def foreach[R](op: T => R): R = {
      try op(resource)
      catch {
        case ex: Exception => throw ex
      }
      finally resource.close()
    }
  }

  override def create(): Unit = {
    if (!file.exists()) {
      try {
        plugin.getDataFolder.mkdirs()
        file.createNewFile()
      } catch {
        case _: IOException =>
      }

      for {
        in <- plugin.getResourceAsStream(fileName)
        out <- new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))))
      } {
        Source.fromInputStream(in).getLines()
          .filterNot(_.startsWith("server-name-in-bungee-network"))
          .foreach(out.println)
      }
    }
  }

  override def save(): Unit = ConfigurationProvider.getProvider(classOf[YamlConfiguration]).save(config, file)

  override def reload(): Unit = {
    try config = ConfigurationProvider.getProvider(classOf[YamlConfiguration]).load(file)
    catch {
      case ex: IOException => ex.printStackTrace()
    }
  }

  override def get(path: String): Any = config.get(path)

  override def section(path: String): HierarchicalConfiguration[Configuration] = new ConfigurationHierarchy(config.getSection(path))

}
