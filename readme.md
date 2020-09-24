# AOP proof of concept
This is a basic currency exchange rate API that displays aspect oriented programming used for logging and data access operations.

### Annotations
- Logged - used to annotate the functions that will be intercepted by LogAspect
- DataAccess - used to annotate the functions that will be intercepted by DataAccessAspect

### Aspect
- LogAspect 
    - Intercepts function calls to functions annotated with `@Logged`. 
    These functions will trigger a log-call before and after function execution (provided function did not throw an exception) with the log level specified in the annotations field value `logLevel`.
- DataAccessAspect
    - Intercepts function calls to functions annotated with `@DataAccess`.
    These functions will wrap the function call in a try/catch, transforming the potential SQL-related exceptions to a custom one along with a KFunction<*> object. This object is used to 
    find the relevant error message for the given function that failed in the error messages resource bundle.

### Setup
Clone repository, start application from `src/main/kotlin/no/esa/aop/application/AopApplication.main`.

### Usage
1. Build & run application in your IDE.
2. View the [Swagger-UI](http://localhost:8096/swagger-ui.html) in a browser
3. Try the requests in the `exchange-rate-controller`.
4. To see how errors are handled, throw a few Exceptions in functions annotated with `@DataAccess`.

## Examples

### Data access operations

#### DataAccessAspect
This is called 'around' every function annotated with `@DataAccess`. `joinPoint.proceed()` calls the intercepted function. Any exception is immediately mapped to a DataAccess exception,
which is picked up by `/resource/ExceptionHandler.kt`.

```kotlin
@Aspect
@Component
class DataAccessAspect {

	@Around("@annotation(no.esa.aop.annotation.DataAccess)")
	fun dataAccessOperation(joinPoint: ProceedingJoinPoint): Any? {
		val kFunction = getKFunction(joinPoint)

		return try {
			joinPoint.proceed()
		} catch (error: Exception) {
			getLogger(joinPoint).error(error.message)

			if (kFunction != null) {
				throw DataAccessException(kFunction, error)
			} else throw error
		}
	}
}
```

#### Function annotation
Abstracting away error handling from virtually identical functions improves readability.

```kotlin
@DataAccess
@Logged(APIType.DATA_ACCESS, LogLevel.INFO)
override fun getAll(): List<CurrencyEntity> {
	val query = QueryFileReader.readSqlFile(::getAll)

	return jdbcTemplate.query(query) { rs, _ ->
		CurrencyEntity(rs.getInt(PRIMARY_KEY), rs.getString(SYMBOL))
	}
}
```

#### DataAccess error logging

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/DataAccess%20console%20error%203.jpg)

#### DataAccess standard logging
![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/Console%201.jpg)

### Standard logging

#### LogAspect
This aspect inspects the function signature and return value and logs the events in a clean format to the console & to file.
The log level and api type are optional parameters that can be defined per function.

```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logged(val apiType: APIType = APIType.INTERNAL,
			val logLevel: LogLevel = LogLevel.DEBUG)
```

#### Various datatypes

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/various%20datatypes.jpg)
