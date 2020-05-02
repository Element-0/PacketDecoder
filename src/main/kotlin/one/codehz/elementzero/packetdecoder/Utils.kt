package one.codehz.elementzero.packetdecoder

import org.sqlite.Function
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.sql.SQLDataException
import java.sql.SQLException
import java.util.*

fun asUUID(bin: ByteArray): UUID {
  val data = ByteBuffer.wrap(bin)
  val high = data.long
  val low = data.long
  return UUID(high, low)
}

fun fromUUID(uuid: UUID): ByteArray {
  val bb = ByteBuffer.wrap(ByteArray(16))
  bb.putLong(uuid.mostSignificantBits)
  bb.putLong(uuid.leastSignificantBits)
  return bb.array()
}


class UUIDFn : Function() {
  override fun xFunc() {
    if (args() != 1) {
      throw SQLException("IsSameDay(date1,date2): Invalid argument count. Requires 1, but found " + args())
    }
    try {
      result(fromUUID(UUID.fromString(value_text(0))))
    } catch (e: Exception) {
      throw SQLDataException("IsSameDay(date1,date2): One of Arguments is invalid: " + e.localizedMessage)
    }
  }
}