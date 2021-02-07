package rocks.ditto.miral.eve_plugin.command

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiLogger
import rocks.ditto.miral.eve_plugin.service.EveService
import java.util.function.Consumer

class Listener: Consumer<GroupMessageEvent> {

    var logger: MiraiLogger? = null

    val cmd = arrayOf("/jita", "/help")

    override fun accept(t: GroupMessageEvent) {
        var group = t.group
        var message = t.message
        var groupId = group.id
        var qq = t.sender.id
        logger = t.bot.logger
        logger!!.info("message:" + message)
    }

    suspend fun processCommand(group: Group, groupId: Long, qq: Long, message: String){
        var result = "";
        if (message.startsWith(cmd[0])){
            // 去除cmd和空格
            message.replace(cmd[0] + " ", "")
            result = EveService.fetchJita(message)
        }
        group.sendMessage(result)
    }

}