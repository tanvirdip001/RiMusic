package it.fast4x.rimusic.utils

import it.fast4x.environment.models.PlayerResponse
import it.fast4x.environment.utils.NewPipeUtils
import timber.log.Timber

fun getSignatureTimestampOrNull(
    videoId: String
): Int? {
    return NewPipeUtils.getSignatureTimestamp(videoId)
        .onFailure {
            Timber.e("NewPipeUtils getSignatureTimestampOrNull Error while getting signature timestamp ${it.stackTraceToString()}")
            println("NewPipeUtils getSignatureTimestampOrNull Error while getting signature timestamp ${it.stackTraceToString()}")
        }
        .getOrNull()
}

fun getStreamUrl(
    format: PlayerResponse.StreamingData.Format,
    videoId: String
): String? {
    var streamUrl = NewPipeUtils.getStreamUrl(format, videoId)
        .onFailure {
            Timber.e("NewPipe Utils getStreamUrlOrNull Error while getting stream url ${it.stackTraceToString()}")
            println("NewPipe Utils getStreamUrlOrNull Error while getting stream url ${it.stackTraceToString()}")
        }
        .getOrNull()

    // 🚀 CLIENT SPOOFING BYPASS: ইউটিউব ব্লকিং বাইপাস করার কাস্টম লজিক
    if (streamUrl != null) {
        if (streamUrl.contains("c=ANDROID_MUSIC")) {
            // ANDROID_MUSIC ক্লায়েন্টকে বদলে WEB_REMIX বা ANDROID (Main App) এ রূপান্তর করা
            streamUrl = streamUrl.replace("c=ANDROID_MUSIC", "c=ANDROID")
            streamUrl = streamUrl.replace("cver=1.2024", "cver=19.23.36") // একটি লেটেস্ট অফিশিয়াল ইউটিউব ভার্সন ফোর্স করা
        }
        // স্ট্রিমিং রেঞ্জ বা থ্রোটলিং প্যারামিটার ফিক্স করার চেষ্টা
        if (!streamUrl.contains("&rn=") && streamUrl.contains("&mws=")) {
            streamUrl = "$streamUrl&rn=1"
        }
    }

    println("NewPipe Utils getStreamUrlOrNull Modified streamUrl $streamUrl")

    return streamUrl
}
data class InvalidHttpCodeException(val code: Int) :
    IllegalStateException("Invalid http code received: $code")

