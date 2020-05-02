package one.codehz.elementzero.packetdecoder

import com.nukkitx.protocol.bedrock.v390.Bedrock_v390
import io.netty.buffer.Unpooled
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class DB(path: String) {
  private val connection = DriverManager.getConnection("jdbc:sqlite:$path")

  fun getSessions(): Sequence<UUID> {
    val statement = connection.createStatement()
    val rs = statement.executeQuery("select distinct session from packets")
    return sequence {
      while (rs.next()) {
        val bin = rs.getBytes(1)
        yield(asUUID(bin))
      }
      statement.close()
    }
  }

  fun decode(): Sequence<Frame> {
    val statement = connection.createStatement()
    val rs =
      statement.executeQuery("select type, idmap.session, address, xuid, time, data from packets join idmap")
    return decodeResultSet(rs, statement)
  }

  fun decodeBySession(session: UUID): Sequence<Frame> {
    val statement = connection.prepareStatement(
      "select type, idmap.session, address, xuid, time, data from packets join idmap where packets.session = ?"
    )
    statement.setBytes(1, fromUUID(session))
    val rs = statement.executeQuery()
    return decodeResultSet(rs, statement)
  }

  private fun decodeResultSet(
    rs: ResultSet,
    statement: Statement
  ): Sequence<Frame> {
    return sequence {
      while (rs.next()) {
        val type = if (rs.getBoolean("type")) FrameType.Sent else FrameType.Received
        val session = asUUID(rs.getBytes("session"))
        val address = rs.getString("address")
        val xuid = rs.getLong("xuid")
        val date = rs.getString("time")
        val bin = rs.getBinaryStream("data")
        try {
          val data = Bedrock_v390.V390_CODEC.tryDecode(Unpooled.wrappedBuffer(bin.readAllBytes()))
          yield(ValidFrame(type, session, date, xuid, address, data))
        } catch (e: Exception) {
          yield(InvalidFrame(type, session, date, xuid, address, e))
        }
      }
      statement.close()
    }
  }
}