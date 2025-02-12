# iGrade - Elektroniczny Dziennik (Backend)

## Autor
[CzarnaWoda](https://github.com/CzarnaWoda/)

## Wstęp
Projekt e-dziennika iGrade został wykonany w czerwcu 2024 roku na potrzeby przedmiotu **Bazy Danych**. Głównym celem projektu było zaprojektowanie aplikacji, która wykorzystuje wiele baz danych, aby zademonstrować możliwości integracji różnych systemów zarządzania bazami danych w architekturze mikroserwisowej.

Dokładniejsza dokumentacja wraz z diagramami oraz przykładami dostępna w pliku **Dokumentacja.pdf**

System obsługuje trzy różne bazy danych: MySQL, PostgreSQL oraz MongoDB, z wykorzystaniem JDBC oraz wbudowanych repozytoriów Spring Boot dla MongoDB. Każda baza danych jest wykorzystywana do przechowywania różnych typów informacji, co pozwala na optymalizację przechowywania danych i ich przetwarzania.

## Technologie oraz biblioteki użyte w backendzie:
- Spring Boot 3.2.6-SNAPSHOT
- Spring Cloud 2023.0.1
- Spring Boot Data JDBC
- Spring Boot Data JPA
- Spring Boot JDBC
- Spring Boot Validation
- Spring Cloud Netflix Eureka Client/Server
- Spring Boot Security
- Spring Boot Actuator
- Spring Cloud CircuitBreaker Resilience4j
- JJWT (io.jsonwebtoken)
- Spring Boot OAuth Authorization Server
- Lombok
- Spring Boot Web
- Spring Boot WebFlux
- Spring Boot Data MongoDB
- MySQL Connector J
- Postman (do testowania endpointów)

## Architektura mikroserwisów
Struktura systemu opiera się na podejściu mikroserwisowym:
1. **API Gateway** - centralny punkt wejścia dla użytkowników, obsługujący autoryzację JWT oraz routing do odpowiednich usług.
2. **Discovery Server (Eureka Server)** - zarządza rejestracją mikroserwisów oraz ich lokalizacją w sieci.
3. **Mikroserwisy**:
   - `user-service` - odpowiada za zarządzanie użytkownikami.
   - `grades-service` - obsługuje oceny uczniów.
   - `notes-service` - obsługuje uwagi nauczycieli.
   - `class-service` - zarządza klasami i ich strukturą.

Każdy mikroserwis jest niezależny i może być skalowany osobno.

## Autoryzacja JWT
Autoryzacja w systemie jest realizowana za pomocą tokenów JWT. Generowanie i weryfikacja tokenów odbywa się w `user-service`, a weryfikacja tokenów przy żądaniach użytkowników odbywa się w `api-gateway`.

## Synchronizacja danych
Aby zapewnić spójność danych między serwisami, wykorzystano komunikację synchroniczną oraz autoryzację tokenów JWT. Każdy mikroserwis może weryfikować tożsamość użytkownika i pobierać jego dane na podstawie tokena.

## Circuit Breaker
System implementuje wzorzec Circuit Breaker (Resilience4j) w celu zabezpieczenia przed przeciążeniem w przypadku awarii jednego z mikroserwisów. 
- **Closed State** - normalne działanie.
- **Open State** - po wykryciu awarii system blokuje żądania do uszkodzonego serwisu.
- **Half-Open State** - testowe żądania są wysyłane w celu sprawdzenia dostępności usługi.

## Struktura projektu
Każdy mikroserwis został podzielony na moduły:
- **Controller** - obsługa żądań HTTP.
- **Service** - logika biznesowa.
- **Repository** - operacje na bazie danych.

## Bazy danych
Ze względu na główny cel projektu, czyli integrację różnych systemów zarządzania bazami danych, wykorzystano:
- **MongoDB** - używane dla dokumentów z uwagami.
- **PostgreSQL** - wykorzystywane dla ocen i klas.
- **MySQL** - przechowuje dane użytkowników.

Dla MySQL i PostgreSQL użyto JPARepository do obsługi operacji na bazach danych.

### Przykładowe zapytania SQL:
- Pobranie oceny ucznia:
  ```sql
  SELECT * FROM Note WHERE studentId = ?;
  ```
- Sumowanie punktów dla ucznia:
  ```sql
  SELECT SUM(points) FROM Note WHERE studentId = ?;
  ```
- Aktualizacja oceny:
  ```sql
  UPDATE Note SET description = ?, points = ? WHERE id = ?;
  ```
- Usunięcie uwagi:
  ```sql
  DELETE FROM Note WHERE id = ?;
  ```

## DTO i Mapowanie
Aby zapewnić bezpieczeństwo danych, zastosowano obiekty DTO, które ograniczają widoczność pól dla użytkowników. 
Przykładowy DTO dla studenta:
```java
public record StudentDTO(String name, String email) {}
```
Mapowanie obiektów bazodanowych na DTO realizowane jest poprzez dedykowane klasy Mapperów.

## Endpointy
### Przykładowe endpointy API
- **Użytkownik pobiera swoje dane**
  ```http
  GET /api/v1/user/me
  Authorization: Bearer {token}
  ```
- **Nauczyciel wystawia ocenę**
  ```http
  POST /api/v1/grades/add
  Content-Type: application/json
  Authorization: Bearer {token}
  ```
  ```json
  {
    "studentId": 123,
    "subject": "Matematyka",
    "points": 5,
    "description": "Sprawdzian"
  }
  ```

## Uruchomienie aplikacji
### Wymagania:
- Java 17+
- Maven
- Docker (opcjonalnie dla baz danych)

### Etapy uruchomienia:
1. Sklonuj projekt:
   ```sh
   git clone https://github.com/MateuszKmiotek/igrade-backend.git
   ```
2. Zbuduj i uruchom serwisy:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## Podsumowanie
Backend aplikacji iGrade został zaprojektowany zgodnie z nowoczesnymi wzorcami architektonicznymi, zapewniając skalowalność, bezpieczeństwo oraz wydajność systemu. Kluczowym założeniem projektu było wykorzystanie różnych baz danych, aby pokazać możliwości ich integracji w systemie mikroserwisowym.

