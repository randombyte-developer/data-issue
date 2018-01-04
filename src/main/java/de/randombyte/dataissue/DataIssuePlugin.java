package de.randombyte.dataissue;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Iterator;

@Plugin(id = "data-issue-plugin")
public class DataIssuePlugin {

    @Inject
    PluginContainer pluginContainer;

    public static Key<Value<Boolean>> BOOL;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        BOOL = KeyFactory.makeSingleKey(
                TypeToken.of(Boolean.class),
                new TypeToken<Value<Boolean>>(){},
                DataQuery.of("Bool"),
                "data-issue-plugin:bool", "Bool");

        Sponge.getDataManager().register(
                SampleData.class,
                SampleData.Immutable.class,
                new SampleData.Builder());
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    Iterator<Entity> iterator = ((Player) src).getNearbyEntities(3.0).iterator();
                    iterator.next(); // ignore the player(in a bad way)
                    Entity entity = iterator.next();

                    DataTransactionResult result = entity.offer(new SampleData(true));
                    System.out.println("Entity type name: " + entity.getType().getName());
                    System.out.println("Data transaction result" + result.toString());

                    return CommandResult.success();
                })
                .build(), "test");
    }
}
