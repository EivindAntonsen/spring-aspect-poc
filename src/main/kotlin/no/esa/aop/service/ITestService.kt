package no.esa.aop.service

interface ITestService {
	fun testUnit()
	fun testIntAsExternalApiType(): Int
	fun testIntWithArgsAsLogLevelInfo(i: Int): Int
	fun testStringAsLogLevelWarn(): String
	fun testStringsAsApiTypeDatabase(): List<String>
	fun testMapAsApiTypeExternalAndLogLevelWarn(): Map<Int, String>
	fun testFunctionAsArgs(f: () -> Unit)
	fun testFunctionAsReturn(): () -> Unit
}
