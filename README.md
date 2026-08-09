# DevBoard

Backend-система для управления задачами пользователей.

Проект реализован на Java и Spring Boot. Предоставляет REST API для регистрации и авторизации пользователей, управления задачами, фильтрации и пагинации.

## Стек

* Java 21
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* PostgreSQL 16
* Maven
* Docker / Docker Compose
* Lombok
* Bean Validation
* Swagger / OpenAPI

## Возможности

### Аутентификация

* Регистрация пользователя
* Авторизация пользователя
* JWT-аутентификация
* Защита API от неавторизованного доступа

### Пользователи

* Создание пользователя
* Получение пользователя по ID
* Обновление пользователя
* Удаление пользователя
* Получение списка пользователей
* Получение задач пользователя

### Задачи

* Создание задачи
* Получение задачи по ID
* Обновление задачи
* Удаление задачи
* Изменение статуса задачи
* Получение задач текущего пользователя
* Фильтрация задач по статусу
* Пагинация
* Сортировка по дате создания

### Обработка ошибок

API использует единый формат ошибок:

```json
{
  "status": 404,
  "message": "Задача с id: 9 не найдена",
  "timestamp": "2026-08-09T22:21:22.0389202"
}
```

Обрабатываются основные ошибки:

* `400 Bad Request` — ошибка валидации
* `401 Unauthorized` — отсутствует или некорректна аутентификация
* `403 Forbidden` — недостаточно прав
* `404 Not Found` — ресурс не найден
* `409 Conflict` — конфликт данных

## Структура проекта

```text
src/main/java/com/dmitry/devboard
├── common
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── DevboardApplication.java
```

### Основные слои

**Controller** — обработка HTTP-запросов.

**Service** — бизнес-логика приложения.

**Repository** — взаимодействие с PostgreSQL через Spring Data JPA.

**Entity** — модели базы данных.

**DTO** — объекты для передачи данных через API.

**Security** — JWT-аутентификация и конфигурация Spring Security.

**Exception** — собственные исключения и глобальная обработка ошибок.

**Config** — конфигурация приложения и OpenAPI.

## Запуск проекта

### Требования

Перед запуском необходимо установить:

* JDK 21
* Docker
* Docker Compose

### 1. Клонирование

```bash
git clone <repository-url>
cd devboard-backend
```

### 2. Настройка переменных окружения

Создайте файл `.env` в корне проекта:

```env
POSTGRES_DB=devboard
POSTGRES_USER=devboard_user
POSTGRES_PASSWORD=devboard_password

JWT_SECRET=<your-base64-secret>
JWT_EXPIRATION=86400000
```

Файл `.env` не должен добавляться в Git.

### 3. Запуск PostgreSQL

```bash
docker compose up -d
```

Проверить состояние контейнера:

```bash
docker compose ps
```

### 4. Запуск Spring Boot

Через Maven:

```bash
./mvnw spring-boot:run
```

Для Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

После запуска приложение будет доступно по адресу:

```text
http://localhost:8080
```

## Swagger

Документация API доступна через Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Для защищённых endpoints необходимо нажать **Authorize** и передать JWT:

```text
Bearer <JWT>
```

Получить JWT можно через:

```text
POST /api/auth/login
```

## API

### Authentication

| Метод | Endpoint             | Описание                    |
| ----- | -------------------- | --------------------------- |
| POST  | `/api/auth/register` | Регистрация                 |
| POST  | `/api/auth/login`    | Авторизация и получение JWT |

### Users

| Метод  | Endpoint                | Описание                     |
| ------ | ----------------------- | ---------------------------- |
| POST   | `/api/users`            | Создание пользователя        |
| GET    | `/api/users`            | Получение всех пользователей |
| GET    | `/api/users/{id}`       | Получение пользователя       |
| PUT    | `/api/users/{id}`       | Обновление пользователя      |
| DELETE | `/api/users/{id}`       | Удаление пользователя        |
| GET    | `/api/users/{id}/tasks` | Получение задач пользователя |

### Tasks

| Метод  | Endpoint                 | Описание                              |
| ------ | ------------------------ | ------------------------------------- |
| POST   | `/api/tasks`             | Создание задачи                       |
| GET    | `/api/tasks/{id}`        | Получение задачи                      |
| PUT    | `/api/tasks/{id}`        | Обновление задачи                     |
| DELETE | `/api/tasks/{id}`        | Удаление задачи                       |
| PATCH  | `/api/tasks/{id}/status` | Изменение статуса                     |
| GET    | `/api/tasks`             | Получение задач текущего пользователя |
| GET    | `/api/tasks/pages`       | Получение задач с пагинацией          |

### Фильтрация

Получение задач определённого статуса:

```text
GET /api/tasks?status=TODO
```

### Пагинация

```text
GET /api/tasks?page=0&size=5
```

По умолчанию для `/api/tasks/pages`:

* размер страницы — `5`;
* сортировка — `createdAt`;
* направление — `DESC`.

## Безопасность

Защищённые endpoints требуют JWT.

JWT передаётся в HTTP-заголовке:

```text
Authorization: Bearer <JWT>
```

Доступ к задачам ограничен владельцем задачи: пользователь не может изменять или удалять задачи другого пользователя.

## Docker

PostgreSQL запускается с помощью Docker Compose.

Данные базы сохраняются в Docker volume:

```text
postgres_data
```

Остановить контейнер:

```bash
docker compose down
```

Запустить снова:

```bash
docker compose up -d
```

## Автор

Dmitry Kirilin
