package amata1219.redis.plugin.messages.bungee.event;

import net.md_5.bungee.api.plugin.Event;

import java.util.List;

public class RedisMessageReceivedEvent extends Event {

    public final String sourceServerName;
    public final String channel;
    public final List<String> message;

    public RedisMessageReceivedEvent(String sourceServerName, String channel, List<String> message) {
        this.sourceServerName = sourceServerName;
        this.channel = channel;
        this.message = message;
    }

}
