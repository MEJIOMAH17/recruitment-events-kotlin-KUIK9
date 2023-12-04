package recruitment.events.deposits

import recruitment.events.external.ExternalSystem
import recruitment.events.messaging.Message
import recruitment.events.messaging.MessageHandler

class DepositMessageHandler(
    private val externalSystem: ExternalSystem,
    private val depositPersistence: DepositPersistence
) : MessageHandler<Deposit> {

    override fun handleMessage(message: Message<Deposit>) {
        handle(message)
        message.acknowledge()
    }

    private fun handle(message: Message<Deposit>) {
        if (message.wasProcessed()) {
            return
        }

        val deposit = message.event
        depositPersistence.save(
            DepositRecord(
                id = deposit.id,
                account = deposit.account,
                amount = deposit.amount,
                version = message.offset
            )
        )
        externalSystem.submitDeposit(deposit)
    }

    private fun Message<Deposit>.wasProcessed(): Boolean {
        return (depositPersistence.versionOf(event.id) ?: Long.MIN_VALUE) >= offset
    }
}
