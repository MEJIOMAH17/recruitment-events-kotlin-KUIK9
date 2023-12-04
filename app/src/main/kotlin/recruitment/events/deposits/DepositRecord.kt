package recruitment.events.deposits

@JvmRecord
data class DepositRecord(val id: String, val account: String, val amount: Long, val version: Long)
