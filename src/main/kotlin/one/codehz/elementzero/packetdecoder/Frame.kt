package one.codehz.elementzero.packetdecoder

import com.nukkitx.protocol.bedrock.BedrockPacket
import java.lang.Exception
import java.util.*

sealed class Frame

data class ValidFrame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String,
  val data: BedrockPacket
) : Frame() {
  override fun toString(): String {
    val builder = StringBuilder()
    builder.append("V")
    builder.append(if (type == FrameType.Received) "R" else "S")
    builder.append(" ")
    builder.append(session.toString())
    builder.append(" ")
    builder.append(time)
    builder.append(" ")
    builder.append(xuid)
    builder.append(" ")
    builder.append(address)
    builder.append(" ")
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
  override fun toString(): String {
    val builder = StringBuilder()
    builder.append("E")
    builder.append(if (type == FrameType.Received) "R" else "S")
    builder.append(" ")
    builder.append(session.toString())
    builder.append(" ")
    builder.append(time)
    builder.append(" ")
    builder.append(xuid)
    builder.append(" ")
    builder.append(address)
    builder.append(" ")
    builder.append(exception)
    return builder.toString()
  }
}