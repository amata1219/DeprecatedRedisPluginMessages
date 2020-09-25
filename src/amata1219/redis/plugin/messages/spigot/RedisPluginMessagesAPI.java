package amata1219.redis.plugin.messages.spigot;

import java.util.Arrays;
import java.util.List;

public interface RedisPluginMessagesAPI {

    void sendRedisPluginMessage(String channel, List<String> message);

    default void sendRedisPluginMessages(String channel, String... message) {
        sendRedisPluginMessage(channel, Arrays.asList(message));
    }

}
