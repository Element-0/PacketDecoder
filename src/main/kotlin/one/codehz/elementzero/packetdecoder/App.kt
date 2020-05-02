package one.codehz.elementzero.packetdecoder

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
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
  private val query by option("--query", help = "Custom query (sql injection :)")
  private val showSession by option("--session", help = "Show session").flag("--no-session", default = true)
  private val showXUID by option("--xuid", help = "Show xuid").flag("--no-xuid", default = true)
  private val showAddress by option("--address", help = "Show address").flag("--no-address", default = false)

  override fun run() {
    val db = DB(database)
    val stream = query?.let { db.decodeByQuery(it) } ?: db.decode()
    val dumper = FrameDumper(
      FrameDumpOption(
        FrameDumpDisplayOption(
          hasSession = showSession,
          hasXUID = showXUID,
          hasAddress = showAddress
        )
      )
    )
    stream.map { dumper.dump(it) }.forEach(::println)
  }
}

fun main(args: Array<String>) = AppCommand().subcommands(GetSessions(), Decode()).main(args)