# Task Management Application

- [Installation](#installation)
- [API Documentation](#api-documentation)
  - [Registration and authentication](#auth)
  - [Account API](#account)
  - [Comment API](#comment)
  - [Task API](#task)
- [SpringDoc(Swagger)](#sw)




## Installation

1. Склонируйте репозиторий с приложением:

    ```bash
    git clone https://github.com/DmBalaev/task-management-application.git
    ```

2. Перейдите в каталог приложения:
    ```bash
    cd task-management-application
    ```
3. Соберите проект:
   ```bash
   mvn clean install -DskipTests=true
   ```
   Опция `-DskipTests=true` в Maven используется для пропуска выполнения тестов во время сборки проекта.


4. Запустите docker контейнер:
    ```bash
    docker-compose up -d
    ```
   Флаг `-d` запускает контейнеры в фоновом режиме.

____

## API Documentation

<a name="auth"></a>
### Authentication API
___

**Аутентификация пользователя**

**URL:** `/api/v1/auth/signin`

**Method**: `POST`

**Request Body**:

   * Type: `AuthRequest`

      ```json
           {
              "email": "your_email",
              "password": "password"
            }

**Responses**:
* `200 OK`: Аутентификация прошла успешно. Возвращает файл AuthResponse, содержащий данные аутентификации.
* `400 Bad Request`: Указан неверный пароль.
* `422 Unprocessable Entity`: Пользователь не существует.

**Security**:
* Доступен всем пользователям
___

**Зарегистрировать пользователя**

**URL**: `/api/v1/auth/signup`

**Method**: `POST`

**Request Body**:

 * Type: `RegistrationRequest`
   ```json
   {
    "name": "your_name",
    "email": "your_email",
    "password": "password"
   }
   ```
**Responses:**

* `200 OK`: Регистрация прошла успешно. Возвращает объект AuthResponse, содержащий регистрационные данные.
* `400 Bad Request`: Имя пользователя уже используется.
Security

**Security**:
* Доступен всем пользователям
___

<a name="account"></a>
### Account Management API

**Получение всех пользователей**

URL: `/api/v1/account`

Method:` GET`

Responses:

* `200 OK`: Возвращает коллекцию объектов AccountInfo, содержащих информацию о всех пользователях.
* `403 Forbidden`: Нет необходимых прав доступа.
----
**Получение пользователя по имени**

URL: `/api/v1/account/{username}`

Method: `GET`

Responses:

* `200 OK`: Возвращает объект AccountInfo, содержащий информацию о пользователе.
* `403 Forbidden`: Нет необходимых прав доступа.
* `404 Not Found`: Пользователь с указанным именем не существует.
Security:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

<a name="comment"></a>
### Comment Management API
**Добавление комментария к задаче**

**URL**: `/api/v1/comment/add/{idTask}`

**Method**: `POST`

**Parameters**:

* `idTask` (Path Parameter): ID задачи, к которой добавляется комментарий.

**Request Body:**

* Type: `Comment`
    ```json
        {
        "text": "Текст комментария"
        }
    ```
**Responses**:

* `201 Created`: Комментарий успешно добавлен. Возвращает объект Comment с добавленным комментарием.
* `404 Not Found`: Задача не найдена.

**Security:**

* Требуется действительный токен JWT (BearerJWT) для аутентификации.

___
**Получение комментариев по задаче**

**URL:** `/api/v1/comment/task/{taskId}`

**Method**: `GET`

**Parameters**:

* `taskId `(Path Parameter): ID задачи, для которой запрашиваются комментарии.
* `page `(Query Parameter, Optional): Номер страницы (с нуля) для получения комментариев.
* `size` (Query Parameter, Optional): Количество элементов на странице.

**Responses**:

* `200 OK`: Возвращает объект Page<Comment>, содержащий комментарии для указанной задачи.
* `404 Not Found`: Задача не найдена.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

<a name="task"></a>
### Task Management API
**Создание задачи**

**URL**: `/api/v1/task`

**Method**: `POST`

**Request Body:**

* Type: TaskRequest
    ```json
      {
        "title": "Название задачи",
        "description": "Описание задачи"
      }
    ```
**Responses**:

* `201 Created`: Задача успешно создана. Возвращает объект Task с созданной задачей.
* `401 Unauthorized`: Пользователь не аутентифицирован.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___
**Получение задачи по ID**

**URL**: `/api/v1/task/{id}`

**Method**: `GET`

**Parameters**:

* `id` (Path Parameter): ID задачи, которую нужно получить.

**Responses**:

* `200 OK`: Задача успешно получена. Возвращает объект Task с задачей.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `404 Not Found`: Задача не найдена.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Получение всех задач**

**URL**: `/api/v1/task`

**Method**: `GET`

**Parameters**:

* `page` (Query Parameter, Optional): Номер страницы (с нуля) для получения задач.
* `size` (Query Parameter, Optional): Количество элементов на странице.
* `status` (Query Parameter, Optional): Фильтр по статусу задачи ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSE').
* `priority` (Query Parameter, Optional): Фильтр по приоритету задачи ('LOW', 'HIGH', 'MEDIUM').

**Responses**:

* `200 OK`: Задачи успешно получены. Возвращает объект Page<Task> с задачами.
* `401 Unauthorized`: Пользователь не аутентифицирован.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Обновление задачи**

**URL**: `/api/v1/task`

**Method**: `PUT`

**Request Body:**

* Type: `TaskUpdateRequest`
    ```json
  {
        "id": "id задачи",
        "title": "Название задачи",
        "description": "Описание задачи"
  }
    ```

**Responses**:

* `200 OK`: Задача успешно обновлена. Возвращает объект Task с обновленной задачей.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача не найдена.
* 
**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Удаление задачи по ID**

**URL**: `/api/v1/task/{taskId}`

**Method**: `DELETE`

**Parameters**:

* `taskId` (Path Parameter): ID задачи для удаления.
**Responses**:

* `204 No Content`: Задача успешно удалена.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача не найдена.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Назначение задачи пользователю**

**URL**: `/api/v1/task/{taskId}/assign/{accountId}`

**Method**: `POST`

**Parameters**:

* `taskId` (Path Parameter): ID задачи для назначения.
* `accountId` (Path Parameter): ID пользователя, которому назначается задача.

**Responses**:

* `200 OK`: Задача успешно назначена. Возвращает объект Task с назначенной задачей.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача или пользователь не найдены.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Снятие задачи с пользователя**

**URL**: `/api/v1/task/{taskId}/unsign`

**Method**: `POST`

**Parameters**:

* `taskId` (Path Parameter): ID задачи для снятия с пользователя.

**Responses**:

* `204 No Content`: Задача успешно снята с пользователя.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача не найдена или не назначена.

**Security**
Требуется действительный токен JWT (BearerJWT) для аутентификации.

___
**Изменение Статуса Задачи**
**URL**: `/api/v1/task/{taskId}/change_status`

**Method**: `POST`

**Parameters**:

* `taskId` (Path Parameter): ID задачи для изменения статуса.

**Request Body:**

```json
{
  "taskStatus": "IN_PROGRESS"
}
```
**Responses**:

* `204 No Content`: Статус задачи успешно изменен.
* `400 Bad Request`: Указан неверный статус задачи.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача не найдена.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___
**Изменение Приоритета Задачи**

**URL**: `/api/v1/task/{taskId}/change_priority`

**Method**: `POST`

**Parameters**:

* `taskId` (Path Parameter): ID задачи для изменения приоритета.

**Request Body:**

* Type: `JsonNode`

    ```json
    {
      "taskPriority": "MEDIUM"
    }
    ```

**Responses**:

* `204 No Content`: Приоритет задачи успешно изменен.
* `400 Bad Request`: Указан неверный приоритет задачи.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Задача не найдена.
* 
**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.

___

**Задачи По Автору**

**URL**: `/api/v1/task/author/{accountId}`

**Method**: `GET`

**Parameters**:

* `accountId` (Path Parameter): ID автора для получения задач.

**Responses**:

* `200 OK`: Задачи автора успешно получены. Возвращает объект Page<Task> с задачами.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Автор не найден.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

**Задачи По Исполнителю**

**URL**: `/api/v1/task/assignee/{accountId}`

**Method**: `GET`

**Parameters**:

* `accountId` (Path Parameter): ID исполнителя для получения задач.

**Responses**:

* `200 OK`: Задачи исполнителя успешно получены. Возвращает объект Page<Task> с задачами.
* `401 Unauthorized`: Пользователь не аутентифицирован.
* `403 Forbidden`: У пользователя нет необходимых разрешений.
* `404 Not Found`: Исполнитель не найден.

**Security**:

Требуется действительный токен JWT (BearerJWT) для аутентификации.
___

<a name="sw"></a>
### Open API (Swagger).
После запуска приложения будет доступен UI
* Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
