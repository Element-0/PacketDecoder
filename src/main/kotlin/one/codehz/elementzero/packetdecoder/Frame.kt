package one.codehz.elementzero.packetdecoder

import com.nukkitx.protocol.bedrock.BedrockPacket
import java.lang.Exception
import java.util.*

data class FrameDumpDisplayOption(
  val hasSession: Boolean = false,
  val hasXUID: Boolean = true,
  val hasAddress: Boolean = false,
  val usePrettyPrint: Boolean = true
)

data class FrameDumpOption(val display: FrameDumpDisplayOption)

sealed class Frame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String
)

class FrameDumper(private val opt: FrameDumpOption) {
  fun dump(frame: Frame): String {
    val builder = StringBuilder()
    builder.append(if (frame is ValidFrame) "V" else "E")
    builder.append(if (frame.type == FrameType.Received) "R" else "S")
    builder.append(" ")
    if (opt.display.hasSession) {
      builder.append(frame.session.toString())
      builder.append(" ")
    }
    builder.append(frame.time)
    builder.append(" ")
    if (opt.display.hasXUID) {
      builder.append(frame.xuid)
      builder.append(" ")
    }
    if (opt.display.hasAddress) {
      builder.append(frame.address)
      builder.append(" ")
    }
    if (frame is ValidFrame)
      builder.append(frame.data)
    else if (frame is InvalidFrame)
      builder.append(frame.exception)
    return builder.toString()
  }
}

class ValidFrame(
  type: FrameType,
  session: UUID,
  time: String,
  xuid: Long,
  address: String,
  val data: BedrockPacket
) : Frame(type, session, time, xuid, address)

class InvalidFrame(
  type: FrameType,
  session: UUID,
  time: String,
  xuid: Long,
  address: String,
  val exception: Exception
) : Frame(type, session, time, xuid, address)