package recruitment.events.deposits

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import recruitment.events.external.ExternalSystem
import recruitment.events.messaging.Message

class DepositMessageHandlerTest {

    @Nested
    class DepositDoesNotExist {
        val persistence = mockk<DepositPersistence>(relaxed = true) {
            every { versionOf(any()) } returns null
        }

        @Test
        fun `saves deposit`() {
            // given
            val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
            val deposit = aDeposit()

            // when
            handler.handleMessage(Message(deposit, 1))

            // then
            verify(exactly = 1) {
                persistence.save(
                    DepositRecord(
                        id = deposit.id,
                        account = deposit.account,
                        amount = deposit.amount,
                        version = 1
                    )
                )
            }
        }

        @Test
        fun `acknowledges message`() {
            // given
            val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
            val deposit = aDeposit()
            val message = mockk<Message<Deposit>> {
                every { event } returns deposit
                every { offset } returns 1
                every { acknowledge() } returns Unit
            }

            // when
            handler.handleMessage(message)

            // then
            verifyOrder {
                persistence.save(any())
                message.acknowledge()
            }
        }

        @Test
        fun `submits deposit to external system`() {
            // given
            val externalSystem = mockk<ExternalSystem>(relaxed = true)
            val handler = DepositMessageHandler(
                externalSystem = externalSystem,
                depositPersistence = persistence
            )
            val deposit = aDeposit()

            // when
            handler.handleMessage(Message(deposit, 1))

            // then
            verify(exactly = 1) {
                externalSystem.submitDeposit(deposit)
            }
        }
    }

    @Nested
    class DepositExist {
        @Nested
        class MessageOffsetLowerThanPersisted() {
            val persistence = mockk<DepositPersistence>(relaxed = true) {
                every { versionOf(any()) } returns 2
            }

            @Test
            fun `does not save deposit`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 0) {
                    persistence.save(any())
                }
            }

            @Test
            fun `acknowledges message`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()
                val message = mockk<Message<Deposit>> {
                    every { event } returns deposit
                    every { offset } returns 1
                    every { acknowledge() } returns Unit
                }

                // when
                handler.handleMessage(message)

                // then
                verify(exactly = 1) {
                    message.acknowledge()
                }
            }

            @Test
            fun `does not submit deposit to external system`() {
                // given
                val externalSystem = mockk<ExternalSystem>(relaxed = true)
                val handler = DepositMessageHandler(
                    externalSystem = externalSystem,
                    depositPersistence = persistence
                )
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 0) {
                    externalSystem.submitDeposit(any())
                }
            }
        }

        @Nested
        class MessageOffsetSameWithPersisted() {
            val persistence = mockk<DepositPersistence>(relaxed = true) {
                every { versionOf(any()) } returns 1
            }

            @Test
            fun `does not save deposit`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 0) {
                    persistence.save(any())
                }
            }

            @Test
            fun `acknowledges message`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()
                val message = mockk<Message<Deposit>> {
                    every { event } returns deposit
                    every { offset } returns 1
                    every { acknowledge() } returns Unit
                }

                // when
                handler.handleMessage(message)

                // then
                verify(exactly = 1) {
                    message.acknowledge()
                }
            }

            @Test
            fun `does not submit deposit to external system`() {
                // given
                val externalSystem = mockk<ExternalSystem>(relaxed = true)
                val handler = DepositMessageHandler(
                    externalSystem = externalSystem,
                    depositPersistence = persistence
                )
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 0) {
                    externalSystem.submitDeposit(any())
                }
            }
        }

        @Nested
        class MessageOffsetGreaterThanPersisted() {
            val persistence = mockk<DepositPersistence>(relaxed = true) {
                every { versionOf(any()) } returns 0
            }

            @Test
            fun `saves deposit`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 1) {
                    persistence.save(
                        DepositRecord(
                            id = deposit.id,
                            account = deposit.account,
                            amount = deposit.amount,
                            version = 1
                        )
                    )
                }
            }

            @Test
            fun `acknowledge message`() {
                // given
                val handler = DepositMessageHandler(externalSystem = ExternalSystem(), depositPersistence = persistence)
                val deposit = aDeposit()
                val message = mockk<Message<Deposit>> {
                    every { event } returns deposit
                    every { offset } returns 1
                    every { acknowledge() } returns Unit
                }

                // when
                handler.handleMessage(message)

                // then
                verifyOrder {
                    persistence.save(any())
                    message.acknowledge()
                }
            }

            @Test
            fun `submits deposit to external system`() {
                // given
                val externalSystem = mockk<ExternalSystem>(relaxed = true)
                val handler = DepositMessageHandler(
                    externalSystem = externalSystem,
                    depositPersistence = persistence
                )
                val deposit = aDeposit()

                // when
                handler.handleMessage(Message(deposit, 1))

                // then
                verify(exactly = 1) {
                    externalSystem.submitDeposit(deposit)
                }
            }
        }
    }
}

private fun aDeposit(): Deposit {
    return Deposit(id = "test", account = "test-acc", amount = 33)
}
