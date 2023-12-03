package recruitment.events.messaging

interface MessageHandler<T : Record> {
    fun resetTo(offset: Long) {}
    fun start() {}
    fun handleMessage(message: Message<T>)
}