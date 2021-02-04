package rocks.ditto.miral.eve_plugin.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand
import rocks.ditto.miral.eve_plugin.rocks.ditto.miral.eve_plugin.PluginMain
import rocks.ditto.miral.eve_plugin.service.EveService

/**
 * jita 命令
 * 获取jita的买单和卖单
 */
object JitaCommand : SimpleCommand(
    PluginMain,
    "jita",
    "j",
    description = "jita"
) {
    @Handler
    suspend fun CommandSender.handle(msg:String) {
        var msg = EveService.fetchJita(msg)
        sendMessage(msg)
    }
}