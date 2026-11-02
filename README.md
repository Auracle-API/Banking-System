# Banking System

Full-stack demo: Spring Boot backend, static HTML/JS frontend.
Supports deposits, withdrawals, and transfers between accounts.

![Screenshot](https://github.com/user-attachments/assets/bbf899e1-f20b-4577-8cba-58c020ea9315)

## Features
- Deposit into an account
- Withdraw (rejected if insufficient funds)
- Transfer between two accounts
- View current balance

## Tech stack
- Java 21
- Spring Boot 3.2
- MySQL 8 (Connector/J 8.4)
- Vanilla HTML/CSS/JS

## Prerequisites
- Java 21, Maven 3.9+, MySQL 8 on localhost:3306

## Setup
1. Create the database: `CREATE DATABASE bank;`
2. Load the schema: `mysql -u root -p bank < schema.sql`
3. Set `DB_USER` and `DB_PASS` environment variables
4. `mvn spring-boot:run`
5. Open http://localhost:8080

## API
| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/accounts/{number} | Get balance |
| POST | /api/accounts/{number}/deposit | Deposit |
| POST | /api/accounts/{number}/withdraw | Withdraw |
| POST | /api/transfers | Transfer |

## Known limitations
- No authentication — not for real use.
- No transaction history.
- Concurrent transfers not protected by row locking.

## What I'd do next
- Authentication and per-user access.
- Transactions table and statement view.
- DB transactions with row locking for transfers.
- Integration tests for the edge cases.
