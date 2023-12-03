package recruitment.events.deposits

import recruitment.events.external.ExternalSystem
import recruitment.events.messaging.Message
import recruitment.events.messaging.MessageHandler


class DepositMessageHandler(
    private val externalSystem: ExternalSystem,
    private val depositPersistence: DepositPersistence
) : MessageHandler<Deposit> {

    override fun handleMessage(message: Message<Deposit>) {
        // implement
    }
}