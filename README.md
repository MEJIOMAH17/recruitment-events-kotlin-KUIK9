# Ex10 Exchange Recruitment Task

## Task Description

Implement a [MessageHandler](app/src/main/kotlin/recruitment/events/messaging/MessageHandler.kt) which
handles [Deposit](app/src/main/kotlin/recruitment/events/deposits/Deposit.kt)
event [Messages](app/src/main/kotlin/recruitment/events/messaging/Message.kt)

The implementation should be added the
skeleton [DepositMessageHandler](app/src/main/kotlin/recruitment/events/deposits/DepositMessageHandler.kt)

### Handling a Deposit

A deposit needs to be sent to an [ExternalSystem](app/src/main/kotlin/recruitment/events/external/ExternalSystem.kt), and
information must be stored about a deposit for later display to users. Persistence should be done using
a [DepositPersistence](app/src/main/kotlin/recruitment/events/deposits/DepositPersistence.kt) instance. For the purpose of this
task, we do not require an implementation of `DepositPersistence`, just adding the required methods to the interface
would suffice.

### Gotchas

1. Messages are delivered with `at-least-once` delivery guarantee.
2. The `ExternalSystem` is not idempotent, it is a critical need to ensure that a deposit is never sent twice.
3. If you wish to make use of a Transactional Persistence Layer, please make it clear what assumptions are made