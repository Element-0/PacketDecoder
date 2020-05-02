package one.codehz.elementzero.packetdecoder

import java.lang.Exception
import java.util.*

sealed class Frame

data class ValidFrame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String,
  val data: String
) : Frame()

data class InvalidFrame(
  val type: FrameType,
  val session: UUID,
  val time: String,
  val xuid: Long,
  val address: String,
  val exception: Exception
) : Frame()