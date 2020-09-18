package one.codehz.elementzero.packetdecoder

import com.nukkitx.network.VarInts
import com.nukkitx.protocol.bedrock.v408.Bedrock_v408
import io.netty.buffer.Unpooled
import org.sqlite.Function
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class DB(path: String) {
  private val connection = DriverManager.getConnection("jdbc:sqlite:$path")

  init {
    Function.create(connection, "uuid", UUIDFn())
  }

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
      statement.executeQuery("select type, idmap.session, address, xuid, time, data from packets join idmap using (session)")
    return decodeResultSet(rs, statement)
  }

  fun decodeByQuery(query: String): Sequence<Frame> {
    val statement = connection.createStatement()
    val rs =
      statement.executeQuery(
        "select * from (select type, idmap.session as session, address, xuid, time, data from packets join idmap using (session)) where $query"
      )
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
          val packetBuffer = Unpooled.wrappedBuffer(bin.readAllBytes())
          val header = VarInts.readUnsignedInt(packetBuffer)
          val packetId = header and 0x3ff
          val data = Bedrock_v408.V408_CODEC.tryDecode(packetBuffer, packetId)
          yield(ValidFrame(type, session, date, xuid, address, data))
        } catch (e: Exception) {
          yield(InvalidFrame(type, session, date, xuid, address, e))
        }
      }
      statement.close()
    }
  }
}
