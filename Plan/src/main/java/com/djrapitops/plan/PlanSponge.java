package com.djrapitops.plan;

import com.djrapitops.plan.api.exceptions.EnableException;
import com.djrapitops.plan.command.PlanCommand;
import com.djrapitops.plan.system.PlanSystem;
import com.djrapitops.plan.system.locale.Locale;
import com.djrapitops.plan.system.locale.lang.PluginLang;
import com.djrapitops.plan.system.settings.theme.PlanColorScheme;
import com.djrapitops.plan.utilities.metrics.BStatsSponge;
import com.djrapitops.plugin.SpongePlugin;
import com.djrapitops.plugin.command.ColorScheme;
import com.djrapitops.plugin.logging.L;
import org.bstats.sponge.Metrics;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.InputStream;

@Plugin(
        id = "plan",
        name = "Plan",
        version = "4.5.0",
        description = "Player Analytics Plugin by Rsl1122",
        authors = {"Rsl1122"},
        dependencies = {
                @Dependency(id = "nucleus", optional = true),
                @Dependency(id = "luckperms", optional = true)
        }
)
public class PlanSponge extends SpongePlugin implements PlanPlugin {

    @com.google.inject.Inject
    private Metrics metrics;

    @com.google.inject.Inject
    private Logger slf4jLogger;

    @com.google.inject.Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;
    private PlanSystem system;
    private Locale locale;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        onEnable();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        onDisable();
    }

    @Override
    public void onEnable() {
        PlanSpongeComponent component = DaggerPlanSpongeComponent.builder().plan(this).build();
        try {
            system = component.system();
            locale = system.getLocaleSystem().getLocale();
            system.enable();

            new BStatsSponge(
                    metrics,
                    system.getDatabaseSystem().getDatabase()
            ).registerMetrics();

            slf4jLogger.info(locale.getString(PluginLang.ENABLED));
        } catch (AbstractMethodError e) {
            slf4jLogger.error("Plugin ran into AbstractMethodError - Server restart is required. Likely cause is updating the jar without a restart.");
        } catch (EnableException e) {
            slf4jLogger.error("----------------------------------------");
            slf4jLogger.error("Error: " + e.getMessage());
            slf4jLogger.error("----------------------------------------");
            slf4jLogger.error("Plugin Failed to Initialize Correctly. If this issue is caused by config settings you can use /plan reload");
            onDisable();
        } catch (Exception e) {
            errorHandler.log(L.CRITICAL, this.getClass(), e);
            slf4jLogger.error("Plugin Failed to Initialize Correctly. If this issue is caused by config settings you can use /plan reload");
            slf4jLogger.error("This error should be reported at https://github.com/Rsl1122/Plan-PlayerAnalytics/issues");
            onDisable();
        }
        PlanCommand command = component.planCommand();
        command.registerCommands();
        registerCommand("plan", command);
    }

    @Override
    public void onDisable() {
        if (system != null) {
            system.disable();
        }

        logger.info(locale.getString(PluginLang.DISABLED));
    }

    @Override
    public InputStream getResource(String resource) {
        return getClass().getResourceAsStream("/" + resource);
    }

    @Override
    public ColorScheme getColorScheme() {
        return PlanColorScheme.create(system.getConfigSystem().getConfig(), logger);
    }

    @Override
    public void onReload() {
        // Nothing to be done, systems are disabled
    }

    @Override
    public boolean isReloading() {
        return false;
    }

    @Override
    public Logger getLogger() {
        return slf4jLogger;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public String getVersion() {
        return getClass().getAnnotation(Plugin.class).version();
    }

    @Override
    public PlanSystem getSystem() {
        return system;
    }

    public Game getGame() {
        return Sponge.getGame();
    }
}
