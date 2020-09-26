package amata1219.redis.plugin.messages.bungee;

import java.util.List;

public interface RedisPluginMessagesAPI {

    void sendRedisPluginMessage(String destinationServerName, String channel, List<String> message);

}
