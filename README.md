# FinWallet Concurrent API
> Digital wallet API built with Java 21, leveraging Virtual Threads, Records and Sealed Interfaces for high-concurrency financial operations.
>
> ##Tech Stack
> - **Java 21** with '--enable-preview' for Pattern Matching
> - **Maven 3.9+** - Build and dependency management
> - **Records** - Immutable domain models with compact constructor validation
> - **Sealed Interfaces** - Exhaustive pattern matching for operation results
> - **Virtual Threads** - Lightweight concurrency for 10k+ simultaneous transfers
> - **Spring Boot 3.3.0** - REST API  + Auto-configuration
> - **Jakarta Validation** - Request validation with detailed error responses
> - **CompletableFuture** - Non-blocking async operations
>
> 
> ##Core Features
> **Zero Deadlocks**: ordered lock acquisition per wallet pair
> **Zero race conditions**: Fine-grained 'ReentrantLock' per wallet ID
> **Non-blocking I/O**: Fraud service simulates 200ms latency using Virtual Threads
> **Producing-ready Errors**: Global exception handler with structured JSON responses
> **API Contract**: Sealed interface 'ProcessResult' for exhaustive pattern matching
> ##Run Locally
>
>###Prerequisites
> - JDK 21+
> - Maven 3.9+
> ```bash
> mvn spring-boot:run
>```
>## Author :
> Yesid AP BACKEND DEVELOPER JAVA
