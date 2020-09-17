# AOP proof of concept
This is a basic currency exchange rate API that displays aspect oriented programming used for logging and data access operations.

### Setup
Clone repository, start application from src/main/kotlin/no/esa/aop/application/AopApplication.main

Should be no setup necessary other than Java 8.

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