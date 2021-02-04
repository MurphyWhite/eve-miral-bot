package rocks.ditto.miral.eve_plugin.rocks.ditto.miral.eve_plugin;

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import rocks.ditto.miral.eve_plugin.command.JitaBuyCommand

object PluginMain : KotlinPlugin(
    JvmPluginDescription("rocks.ditto.my-plugin", "1.0")
) {

    override fun onEnable() {
        PluginMain.logger.info("eve plugin load ")
        JitaBuyCommand.register()
    }

    override fun onDisable() {
        JitaBuyCommand.unregister();
    }
}
