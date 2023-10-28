package me.uniodex.velocityrcon.commandsource;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.velocitypowered.api.permission.PermissionFunction.ALWAYS_TRUE;

public class IRconCommandSource implements RconCommandSource {

    private final List<Component> buffer = new ArrayList<>();
    private PermissionFunction permissionFunction = ALWAYS_TRUE;

    @Getter
    private ProxyServer server;

    public IRconCommandSource(ProxyServer server) {
        this.server = server;
    }

    private void addToBuffer(Component message) {
        message = GlobalTranslator.render(message, Locale.getDefault());
        buffer.add(message);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message) {
        addToBuffer(message);
    }

    @Override
    public void sendMessage(@NonNull Component message) {
        addToBuffer(message);
    }

    @Override
    public @NonNull Tristate getPermissionValue(@NonNull String permission) {
        return this.permissionFunction.getPermissionValue(permission);
    }

    public Component flush() {
        Component all = Component.text("");
	    for (int i = 0; i < buffer.size(); i++) {
            if (i > 0) {
                all = all.append(Component.text("\n"));
            }
		    all = all.append(buffer.get(i));
	    }
        buffer.clear();
        return all;
    }
}
