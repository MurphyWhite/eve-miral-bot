package rocks.ditto.miral.eve_plugin.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import rocks.ditto.miral.eve_plugin.rocks.ditto.miral.eve_plugin.PluginMain

object JitaBuyCommand : SimpleCommand(
    PluginMain,
    "jitabuy",
    "a",
    description = "jitabuy测试"
) {
    @Handler
    suspend fun CommandSender.handle() {
        val msg = "jitabuy response"
        sendMessage(msg)
    }
}