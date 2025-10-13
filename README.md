# Notes Practicum (Java Web, без потоков)

## Цель
С нуля поднять HTTP-сервис в 4 вариантах:
1) «Голые» сокеты (JDK, без фреймворков)
2) Сервлеты + внешний Tomcat (WAR)
3) Сервлеты + embedded Tomcat (запуск из `main`)
4) Spring Boot MVC (+ Swagger UI)

Во всех вариантах — **одно и то же API**: CRUD заметок.  
Данные — в памяти. Тестирование — едиными автотестами.

---

## Модель и эндпоинты

**Note:**
- `id: string (UUID)`
- `title: string` _(1..100, required)_
- `content: string?` _(<=200)_
- `createdAt: string (ISO-8601)`

**Эндпоинты:**
- `GET /health` → `{ "status": "ok" }`
- `GET /notes` → `Note[]`
- `POST /notes` (NoteCreate) → `201 + Note` (+ `Location: /notes/{id}`)
- `GET /notes/{id}` → `200 + Note` / `404`
- `PUT /notes/{id}` (NoteUpdate) → `200 + Note` / `404`
- `DELETE /notes/{id}` → `204` / `404`

**Форматы и ошибки**
- Все ответы — `application/json`
- Пустой `title` → `400`
- Неизвестный `id` → `404`

Спецификация: [`openapi/openapi.yaml`](openapi/openapi.yaml)

---

## Критерии приёмки

* Все 4 реализации проходят автотесты api-tests.
* POST /notes возвращает 201 + Location + createdAt.
* Валидация: title пустой → 400.
* HTML-страница ui/notes.html работает со всеми реализациями (через Base URL).

---

### Дополнительно (по желанию)

* GET /notes?q=term — фильтр по title.
* Простая статика по / (HTML с кратким описанием).
* CORS-заголовки (для фронта из другого origin).

---

## Как запускать

### Общее
```bash
# корень
./gradlew tasks
```

### Stage1 — Sockets
```bash
./gradlew :stage1-sockets:run
./gradlew :api-tests:test -DbaseUrl=http://localhost:8080
open ui/notes.html   # ручная проверка (укажите Base URL)
```

### Stage2 — WAR на внешнем Tomcat 10.1+
```bash
./gradlew :stage2-servlet-war:war
# скопируйте build/libs/ROOT.war → $TOMCAT/webapps/
# стартуйте Tomcat
./gradlew :api-tests:test -DbaseUrl=http://localhost:8080
```

### Stage3 — Embedded Tomcat
```bash
./gradlew :stage3-servlet-embedded:run
./gradlew :api-tests:test -DbaseUrl=http://localhost:8080
```

### Stage4 — Spring Boot MVC (+ Swagger UI)
```bash
./gradlew :stage4-spring-boot:bootRun
open http://localhost:8080/swagger
./gradlew :api-tests:test -DbaseUrl=http://localhost:8080
```
