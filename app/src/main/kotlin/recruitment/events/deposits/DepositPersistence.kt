package recruitment.events.deposits

interface DepositPersistence {
    /**
     * assumption: if method executed without exceptions, then every subsequent invocation versionOf will return
     * deposit.version until next "save" with same deposit.id call will override it
     */
    fun save(deposit: DepositRecord)
    fun versionOf(id: String): Long?
}
