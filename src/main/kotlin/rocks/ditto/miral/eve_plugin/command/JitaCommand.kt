package rocks.ditto.miral.eve_plugin.command

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.MiraiLogger
import rocks.ditto.miral.eve_plugin.rocks.ditto.miral.eve_plugin.PluginMain
import rocks.ditto.miral.eve_plugin.service.EveService
import java.lang.StringBuilder

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
    private val logger: MiraiLogger = MiraiConsole.createLogger(this.javaClass.name)

    suspend fun CommandSender.handle(msg:String) {
        var msg = EveService.fetchJita(msg)
        sendMessage(msg)
    }

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        var text = args.joinToString(" ")
        var msg = EveService.fetchJitaNew(text)
        sendMessage(msg)
    }
}