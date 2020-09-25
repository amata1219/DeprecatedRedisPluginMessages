package amata1219.redis.plugin.messages.spigot.event;

import com.sun.istack.internal.NotNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import scala.collection.immutable.List;

public class RedisMessageReceivedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public final String sourceServerName;
    public final String channel;
    public final List<String> message;

    public RedisMessageReceivedEvent(String sourceServerName, String channel, List<String> message) {
        this.sourceServerName = sourceServerName;
        this.channel = channel;
        this.message = message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
