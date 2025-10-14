# Library — Spring MVC + JPA (Thymeleaf)

Небольшое веб-приложение для управления библиотекой: читатели (People) и книги (Books), с назначением/возвратом книг, поиском, пагинацией и сортировкой.

## Стек

- Java 8+  
- Spring 5: Web MVC, Data JPA, JDBC, Validation  
- Hibernate (JPA provider)  
- Thymeleaf (server-side шаблоны)  
- PostgreSQL  
- Maven (`war` packaging), деплой на Tomcat/совместимый контейнер сервлетов  
- Фильтры: `CharacterEncodingFilter` (UTF-8), `HiddenHttpMethodFilter` (PATCH/DELETE в HTML-формах)

## Возможности

- **CRUD для читателей (People)**  
  Список, карточка, создание, редактирование, удаление. Уникальность ФИО (серверная валидация).

- **CRUD для книг (Books)**  
  Список, карточка, создание, редактирование, удаление. Уникальность названия (серверная валидация).

- **Выдача/возврат книг**
  - `PATCH /books/{id}/assign` — назначение книги читателю, отметка `takenAt = today`.
  - `PATCH /books/{id}/release` — освобождение книги.
  - Для каждого читателя на странице профиля отображаются его книги; если книга у читателя **более 10 дней**, она помечается как **просроченная** (`expired = true`).

- **Поиск книг по префиксу названия**  
  Страница `/books/search` + форма `POST /books/search` (репозиторий: `searchBooksByTitleStartingWith`).

- **Пагинация и сортировка**  
  Для списков предусмотрены параметры:
  - `page` (0-based), `size` — постраничный вывод;
  - `sort=true` — для книг сортировка по году публикации (`yearOfPublication`).

- **Серверная валидация форм**  
  Hibernate Validator: обязательные поля, размеры строк/паттерны/диапазоны, уникальность имени/названия через `PersonValidator`/`BookValidator`.

- **Чистая архитектура**  
  Слои Controller → Service → Repository, JPA-сущности и серверный рендеринг через Thymeleaf.

## Архитектура пакетов
org.example.config # Java-конфигурация Spring, JPA, Thymeleaf, фильтры
org.example.controllers # BooksController, PeopleController (маршрутизация и view-модели)
org.example.models # JPA-сущности: Book, Person
org.example.repositories# Spring Data JPA репозитории
org.example.services # Бизнес-логика: выдача/возврат, поиск, пагинация/сортировка, overdue-логика
org.example.util # Валидаторы: PersonValidator, BookValidator


## Модели данных (по коду)

> Ниже — ключевые поля; валидация выполняется как аннотациями (Size/Pattern/Min/Max/NotNull), так и кастом-валидаторами.

**Person**
- `id:int` (PK)
- `name:String` — **уникально** (валидатор), строковые ограничения
- `yearOfBirth:int`

**Book**
- `id:int` (PK)
- `title:String` — **уникально** (валидатор), строковые ограничения
- `author:String`
- `yearOfPublication:int`
- `owner:Person` (ManyToOne)
- `takenAt:Date` — дата выдачи
- `expired:boolean` — «просрочена», вычисляется при показе книг у читателя (если > 10 дней со дня выдачи)

## Маршруты (основные)

> Базовые префиксы контроллеров: `/people`, `/books`. Для PATCH/DELETE в HTML-формах используется `HiddenHttpMethodFilter`.

### People
- `GET  /people` — список (опц. `page`, `size`)
- `GET  /people/new` — форма создания
- `POST /people` — создать
- `GET  /people/{id}` — карточка + список его книг (с пометкой «просрочено», если нужно)
- `GET  /people/{id}/edit` — форма редактирования
- `PATCH/people/{id}` — обновить
- `DELETE /people/{id}` — удалить

### Books
- `GET  /books` — список (опц. `page`, `size`, `sort=true` — сортировка по году)
- `GET  /books/new` — форма создания
- `POST /books` — создать
- `GET  /books/{id}` — карточка (владелец, выдача/возврат)
- `GET  /books/{id}/edit` — форма редактирования
- `PATCH/books/{id}` — обновить
- `DELETE /books/{id}` — удалить
- `PATCH/books/{id}/assign` — назначить владельца (из select со списком читателей)
- `PATCH/books/{id}/release` — освободить книгу
- `GET  /books/search` — страница поиска
- `POST /books/search` — выполнить поиск по `query` (префикс названия)

## Настройка БД

Файл `src/main/resources/hibernate.properties`:
```properties
hibernate.driver_class=org.postgresql.Driver
hibernate.connection.url=jdbc:postgresql://localhost:5432/library
hibernate.connection.username=postgres
hibernate.connection.password=2606

hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=true
# при необходимости добавьте:
# hibernate.hbm2ddl.auto=update
