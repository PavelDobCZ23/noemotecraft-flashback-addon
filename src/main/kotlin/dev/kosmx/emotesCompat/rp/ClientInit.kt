package dev.kosmx.emotesCompat.rp

import io.github.kosmx.emotes.api.events.client.ClientEmoteEvents

fun initializeClient() {
    ClientEmoteEvents.EMOTE_PLAY.register { emoteData, userID -> saveC2SEmotePacket(emoteData, userID) }
    ClientEmoteEvents.LOCAL_EMOTE_STOP.register { saveC2SStopPacket() }
}