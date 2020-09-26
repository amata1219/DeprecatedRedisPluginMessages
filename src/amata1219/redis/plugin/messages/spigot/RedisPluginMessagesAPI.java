package amata1219.redis.plugin.messages.spigot;

import java.util.List;

public interface RedisPluginMessagesAPI {

    void sendRedisPluginMessage(String channel, List<String> message);

}
