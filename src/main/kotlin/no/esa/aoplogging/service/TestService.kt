package no.esa.aoplogging.service

import no.esa.aoplogging.annotation.Logged
import no.esa.aoplogging.enums.APIType
import org.springframework.boot.logging.LogLevel
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class TestService : ITestService {

	companion object {
		private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
				"Etiam felis leo, placerat ut tristique porttitor, rutrum cursus dui. " +
				"Aliquam malesuada posuere purus at volutpat. " +
				"Donec vel mi vitae lacus cursus laoreet ac vel elit. " +
				"Sed et lobortis sem. " +
				"Aliquam convallis imperdiet ligula ac sollicitudin. " +
				"Vestibulum quis dui sem."
	}

	@Logged
	override fun testUnit() {
		return
	}

	@Logged(apiType = APIType.EXTERNAL)
	override fun testIntAsExternalApiType(): Int {
		return 5
	}

	@Logged(logLevel = LogLevel.INFO)
	override fun testIntWithArgsAsLogLevelInfo(i: Int): Int {
		return i
	}

	@Logged(logLevel = LogLevel.WARN)
	override fun testStringAsLogLevelWarn(): String {
		return LOREM_IPSUM
	}

	@Logged(APIType.DATA_ACCESS)
	override fun testStringsAsApiTypeDatabase(): List<String> {
		return LOREM_IPSUM.split(".")
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.WARN)
	override fun testMapAsApiTypeExternalAndLogLevelWarn(): Map<Int, String> {
		return LOREM_IPSUM.split(".").map { line ->
			Random.nextInt(1, 10) to line
		}.toMap()
	}

	@Logged
	override fun testFunctionAsArgs(f: () -> Unit) {
		return
	}

	@Logged
	override fun testFunctionAsReturn(): () -> Unit {
		return { println("this is a function that probably won't be run.") }
	}
}
