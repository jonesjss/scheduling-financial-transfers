# Scheduling Financial Transfers

The purpose of this project is to carry out the transfer between accounts by calculating an interest rate according to the scheduling date. 

## Language Versions
- **`Java:17`**: Platform used in the back end.

- **`Spring Data JPA (2.6.3)`**: Part of the larger Spring Data family, makes it easy to easily implement JPA based repositories.

- **`Spring Starter Web (2.6.3)`**:  Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container.

- **`Spring Starter Validation (2.6.3)`**:  Starter for using Java Bean Validation with Hibernate Validator 

- **`Spring Devtools (2.6.3)`**: The Spring Boot DevTools module includes an embedded server called LiveReload. It allows the application to automictically trigger a browser refresh whenever we make changes in the resources. It is also known as auto-refresh.

- **`Spring Starter Test: (2.6.3)`**: Is the primary dependency for the test. It contains the majority of elements required for our tests.

- **`FlywayDB (8.0.5)`**: Flyway is the open-source tool that makes database migrations easy.

- **`H2 Database (1.4.200)`**: Embedded and server modes; disk-based or in-memory databases

- **`SpringFox Swagger2 (2.9.2)`**: It automates the generation of specifications for JSON APIs, implemented with the Spring framework. Also, it provides libraries to integrate the Swagger UI to interact with APIs.

- **`Lombok (1.18.22)`**: Is a java library that automatically plugs into your editor and build tools,

- **`MapStruct (1.4.2.Final)`**: Is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach.

- **`REST-assured (4.4.0)`**: Testing and validation of REST services in Java is harder than in dynamic languages such as Ruby and Groovy. 

## Design Patterns
### **Creational**

- **`Builder`**: It was used to be able to create objects for testing.

**Example**
````java
Account.builder()
      .id(1L)
      .user(user)
      .agency("0001")
      .accountNumber("123456")
      .build();
````

- **`Factory`**: It was used to build an object factory for testing

**Example**

````java
public class AccountFactory {

    private AccountFactory() {}

    public static Account createAccountOriginatingSaved(User user) {
        // code omitted
        return account;
    }
}
````

### Structural
- **`Adapter`**: it was used to handle the api exception responses 

**Example**

````java
@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {

        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }
    // code omitted
}
````

### Behavioral
- **`Strategy`**: it was used to apply the transfer tax rule 

**Example**

```java

public abstract class TransferStrategy {
  // code omitted

  protected abstract BigDecimal calculateRate(TransferValue transferValue);
}

// This class is responsible for calculating the interest rate for transfers made on the same day 
public class TransferMadeOnTheSameDayStrategy extends TransferStrategy {
    // code ommited
    protected BigDecimal calculateRate(TransferValue transferValue) {
        log.info("Applied calculation is: same day.");

        BigDecimal rate = new BigDecimal("0.03")
                .multiply(transferValue.getTransferValue())
                .add(new BigDecimal("3"))
                .setScale(2, RoundingMode.HALF_EVEN);

        return rate;
    }
}
```

- **`Template Method`**: It was identified that there is a pattern between the sequence of validation and application of the interest rate 

**Example**

```java
public abstract class TransferStrategy {
    // code omitted
    public CalculatedTransfer calculate(TransferValue transferValue) {
        if(isCalculate(transferValue)) {
            // code ommited
            return calculatedTransfer;
        } else if(!Objects.isNull(getNext())) {
            return this.getNext().calculate(transferValue);
        }
        throw new TransferException("Could not find a calculation for this transfer");
    }

    protected abstract boolean isCalculate(TransferValue transferValue);
    protected abstract BigDecimal calculateRate(TransferValue transferValue);
}

// This class is responsible for calculating the interest rate for transfers made on the same day 
public class TransferMadeOnTheSameDayStrategy extends TransferStrategy {
    // code ommited

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return differenceInDays == 0;
    }

    @Override
    protected BigDecimal calculateRate(TransferValue transferValue) {
        // code omitted
        return rate;
    }
}

```

- **`Chain of Responsability`**: This pattern was used because there are several conditionals to be able to find which type of interest rate to apply in the transfer 

**Example**

```java
public class TransferCalculation {

    public static CalculatedTransfer calculateRate(TransferValue transferValue) {
        TransferStrategy transferOver40DaysAndAmountOver100ThousandStrategy = new TransferOver40DaysAndAmountOver100ThousandStrategy();
        TransferStrategy transferOver30To40DaysStrategy = new TransferOver30To40DaysStrategy(transferOver40DaysAndAmountOver100ThousandStrategy);
        TransferStrategy transferOver20To30DaysStrategy = new TransferOver20To30DaysStrategy(transferOver30To40DaysStrategy);
        TransferStrategy transferOver10To20DaysStrategy = new TransferOver10To20DaysStrategy(transferOver20To30DaysStrategy);
        TransferStrategy transferInUpTo10DaysStrategy = new TransferInUpTo10DaysStrategy(transferOver10To20DaysStrategy);
        TransferStrategy transferMadeOnTheSameDayStrategy = new TransferMadeOnTheSameDayStrategy(transferInUpTo10DaysStrategy);

        return transferMadeOnTheSameDayStrategy.calculate(transferValue);
    }
}

public abstract class TransferStrategy {
    // code omitted
    
    protected abstract boolean isCalculate(TransferValue transferValue);
}

public class TransferMadeOnTheSameDayStrategy extends TransferStrategy {

    // code omitted

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return differenceInDays == 0;
    }

    // code omitted
}

```

## Instructions To Up The Project 

### Prerequisites

**You must have installed java version 17 or higher**

### Profiles
The project has 3 profile environments:
````
-Dspring-boot.run.profiles=    **(this is default)**
-Dspring-boot.run.profiles=dev
-Dspring-boot.run.profiles=prd
````

### Steps
~~~
1. git clone https://github.com/jonesjss/scheduling-financial-transfers.git

2. cd scheduling-financial-transfers/

3. ./mvnw clean compile

4. ./mvnw spring-boot:run -Dspring-boot.run.profiles=prd
~~~

### Accessing the api

After the project started, 2 users and 2 accounts will be configured, just access the link below and test:

````
http://localhost:8080/swagger-ui.html
````
