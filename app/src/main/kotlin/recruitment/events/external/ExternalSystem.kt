package recruitment.events.external

import recruitment.events.deposits.Deposit

class ExternalSystem {
    /*
     NOT IDEMPOTENT!!
     */
    fun submitDeposit(deposit: Deposit?) {}
}
