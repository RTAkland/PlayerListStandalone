/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/19 22:31
 */

package cn.rtast.playerlist

import cn.rtast.playerlist.errs.AuthException
import cn.rtast.playerlist.errs.ConnException
import cn.rtast.playerlist.models.Data
import cn.rtast.playerlist.models.PlayerInfoModel
import com.google.gson.Gson
import nl.vv32.rcon.Rcon
import java.net.ConnectException
import java.net.URL


class GetPlayerList(
    private val host: String,
    private val port: Int,
    private val password: String
) : Runnable {

    private val gson = Gson()

    private fun runCommand() {
        try {
            val rcon = Rcon.open(host, port)
            if (rcon.authenticate(password)) {
                val originPlayers = rcon.sendCommand("whitelist list")
                val players = originPlayers.split(": ").last().split(", ")
//        val players = listOf("RTA", "xiaoman", "Durex")
        val dataList = mutableListOf<Data>()
        players.forEach {
            dataList.add(Data(it))
        }
        val playerModel = PlayerInfoModel(dataList)
        val json = gson.toJson(playerModel).toString()
        try {
            URL("https://realtime.deta.dev/api/update?p=$json").readText()
        } catch (_: Exception) {
            println("无法连接服务器")
        }
                rcon.close()
            } else {
                throw AuthException("错误的密码: $password")
            }
        } catch (_: ConnectException) {
            throw ConnException("无法连接到 $host:$port")
        }
    }

    override fun run() {
        while (true) {
            runCommand()
            Thread.sleep(5000)
        }
    }
}


fun main(args: Array<String>) {
    GetPlayerList(args[0], args[1].toInt(), args[2]).run()
}