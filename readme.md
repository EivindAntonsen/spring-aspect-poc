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
3. Try the requests in the `exchange-rate-controller` multiple times.
    * they will at times fail, and this is intentional - the point is to see how the failures are handled.
4. To disable random failures, remove all usages of `/Utils/DevUtils.maybeFail()`  from code.

## Examples

### Data access operations

#### DataAccessAspect
This is called 'around' every function annotated with `@DataAccess`. `joinPoint.proceed()` calls the intercepted function. Any exception is immediately mapped to a DataAccess exception,
which is picked up by `/resource/ExceptionHandler.kt`.

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/DataAccess%20Aspect.jpg)

#### Function annotation
Abstracting away error handling from virtually identical functions improves readability.

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/DataAccess%20function%203.jpg)

#### DataAccess error logging

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/DataAccess%20console%20error%203.jpg)

#### DataAccess standard logging
![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/Console%201.jpg)

### Standard logging

#### LogAspect
This aspect inspects the function signature and return value and logs the events in a clean format to the console & to file.
The log level and api type are optional parameters that can be defined per function.

![](https://github.com/EivindAntonsen/spring-aspect-poc/blob/master/examples/Logged%20function%20example%201.jpg)
