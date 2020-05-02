package one.codehz.elementzero.packetdecoder

import com.nukkitx.protocol.bedrock.BedrockPacket
import java.lang.Exception
import java.util.*

data class FrameDumpDisplayOption(val hasSession: Boolean = false, val hasXUID: Boolean = true, val hasAddress: Boolean = false)

data class FrameDumpOption(val display: FrameDumpDisplayOption)

sealed class Frame {
  abstract fun dump(opt: FrameDumpOption): String
}

data class ValidFrame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String,
  val data: BedrockPacket
) : Frame() {
  override fun dump(opt: FrameDumpOption): String {
    val builder = StringBuilder()
    builder.append("V")
    builder.append(if (type == FrameType.Received) "R" else "S")
    builder.append(" ")
    if (opt.display.hasSession) {
      builder.append(session.toString())
      builder.append(" ")
    }
    builder.append(time)
    builder.append(" ")
    if (opt.display.hasXUID) {
      builder.append(xuid)
      builder.append(" ")
    }
    if (opt.display.hasAddress) {
      builder.append(address)
      builder.append(" ")
    }
    builder.append(data)
    return builder.toString()
  }
}

data class InvalidFrame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String,
  val exception: Exception
) : Frame() {
  override fun dump(opt: FrameDumpOption): String {
    val builder = StringBuilder()
    builder.append("E")
    builder.append(if (type == FrameType.Received) "R" else "S")
    builder.append(" ")
    if (opt.display.hasSession) {
      builder.append(session.toString())
      builder.append(" ")
    }
    builder.append(time)
    builder.append(" ")
    if (opt.display.hasXUID) {
      builder.append(xuid)
      builder.append(" ")
    }
    if (opt.display.hasAddress) {
      builder.append(address)
      builder.append(" ")
    }
    builder.append(exception)
    return builder.toString()
  }
}