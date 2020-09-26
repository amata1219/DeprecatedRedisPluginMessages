package amata1219.redis.plugin.messages.bungee;

import java.util.Arrays;
import java.util.List;

public interface RedisPluginMessagesAPI {

    void sendRedisPluginMessage(String destinationServerName, String channel, List<String> message);

    default void sendRedisPluginMessage(String destinationServerName, String channel, String... message) {
        sendRedisPluginMessage(destinationServerName, channel, Arrays.asList(message));
    }

}
