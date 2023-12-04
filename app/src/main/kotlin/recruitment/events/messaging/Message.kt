package recruitment.events.messaging

@JvmRecord
data class Message<T : Record>(val event: T, val offset: Long) {
    /*
    updates the underlying stream tip to the offset this message
    so that messages before this will not be replayed to this consumer on reconnection
     */
    fun acknowledge() {}
}
