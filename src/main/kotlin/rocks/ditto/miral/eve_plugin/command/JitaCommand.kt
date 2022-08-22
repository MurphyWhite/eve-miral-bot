package rocks.ditto.miral.eve_plugin.command

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.MiraiLogger
import rocks.ditto.miral.eve_plugin.PluginMain
import rocks.ditto.miral.eve_plugin.service.EveService

/**
 * jita 命令
 * 获取jita的买单和卖单
 */
object JitaCommand : RawCommand(
    PluginMain,
    "jita",
    "j",
    description = "jita"
) {
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        var text = args.joinToString(" ")
        var msg = EveService.fetchJitaNew(text)
        sendMessage(msg)
    }
}