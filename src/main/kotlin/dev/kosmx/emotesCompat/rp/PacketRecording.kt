package dev.kosmx.emotesCompat.rp

import com.replaymod.recording.ReplayModRecording
import dev.kosmx.playerAnim.core.data.KeyframeAnimation
import io.github.kosmx.emotes.api.proxy.INetworkInstance
import io.github.kosmx.emotes.common.network.EmotePacket
import io.github.kosmx.emotes.fabric.network.EmoteCustomPayload
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import java.util.UUID

var currentId: UUID? = null

fun saveC2SEmotePacket(emoteData: KeyframeAnimation?, player: UUID) {
    if (MinecraftClient.getInstance().player?.uuid == player && emoteData != null) {
        currentId = emoteData.uuid
        ReplayModRecording.instance?.connectionEventHandler?.packetListener?.save(
            EmotePacket.Builder()
                .configureToStreamEmote(emoteData, player)
                .build().write().let { emotePacket ->
                    val data = INetworkInstance.safeGetBytesFromBuffer(emotePacket)

                    ServerPlayNetworking.createS2CPacket(EmoteCustomPayload(data))
                }
        )
    }
}

fun saveC2SStopPacket() {
    val player = MinecraftClient.getInstance().player?.uuid

    if (MinecraftClient.getInstance().player?.isMainPlayer == true && player != null) {
        ReplayModRecording.instance?.connectionEventHandler?.packetListener?.save(
            EmotePacket.Builder()
                .configureToSendStop(currentId, player)
                .build().write().let {
                    val data = INetworkInstance.safeGetBytesFromBuffer(it)

                    ServerPlayNetworking.createS2CPacket(EmoteCustomPayload(data))
                }
        )
    }
}
