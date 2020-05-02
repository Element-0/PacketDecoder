package one.codehz.elementzero.packetdecoder

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.util.*

class AppCommand : CliktCommand("ElementZero packet decoder", name = "packetdecode") {
  override fun run() = Unit
}

class GetSessions : CliktCommand("Get sessions from database", name = "get-sessions") {
  private val database by option("--db", help = "Database path").default("packet.db")

  override fun run() {
    val db = DB(database)
    db.getSessions().map(UUID::toString).forEach(::println)
  }
}

class Decode : CliktCommand("Decode database", name = "decode") {
  private val database by option("--db", help = "Database path").default("packet.db")
  private val session by option("--session", help = "Session uuid").convert { UUID.fromString(it) }

  override fun run() {
    val db = DB(database)
    val stream = session?.let { db.decodeBySession(it) } ?: db.decode()
    stream.map { it.toString() }.forEach(::println)
  }
}

fun main(args: Array<String>) = AppCommand().subcommands(GetSessions(), Decode()).main(args)