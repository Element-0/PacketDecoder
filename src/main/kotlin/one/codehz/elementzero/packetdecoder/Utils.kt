package one.codehz.elementzero.packetdecoder

import java.io.InputStream
import java.nio.ByteBuffer
import java.util.*

fun asUUID(bin: ByteArray): UUID {
  val data = ByteBuffer.wrap(bin);
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