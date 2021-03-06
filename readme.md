Тестовая задача по Scala
=
Разработать веб-сервис поиска по заданным ключевым словам через внешний веб-сервис.

Задача:
-
Необходимо разработать сервис, реализующее следующую функцию:
 
1. Обслуживать _HTTP_ запросы по _URL_ `/search`. В параметрах запроса передается параметр `tag`, содержащий ключевой тэг для поиска.
Параметров может быть несколько, в этом случае мы работаем с несколькими ключевыми тэгами.
Пример:
[http://localhost:8080/search?tag=clojure&tag=scala](http://localhost:8080/search?tag=clojure&tag=scala).
Предполагаем, что клиент будет передавать только алфавитно-цифровые запросы в _ASCII_.
Однако, наличие корректной поддержки русского языка в кодировке _UTF-8_ будет плюсом.

2. Сервис должен обращаться к _REST API StackOverflow_ для поиска _[(документация по API)](https://api.stackexchange.com/docs/search)_.
В случае, если ключевых слов передано больше одного, запросы должны выполняться параллельно
_(по одному HTTP запросу на ключевое слово)_.
Должно быть ограничение на максимальное количество одновременных HTTP-соединений, это значение нельзя превышать.
Если ключевых слов больше, нужно организовать очередь обработки так,
чтобы более указанного количество соединений не открывалось.
По каждому тэгу ищем только первые 100 записей, отсортированных по дате создания.
Пример запроса к API:
https://api.stackexchange.com/2.2/search?pagesize=100&order=desc&sort=creation&tagged=clojure&site=stackoverflow.
Можно использовать любые дополнительные параметры запроса, если это необходимо.

3. В результатах поиска интересует полный список тегов по каждому вопросу, а также был ли дан на вопрос ответ.

4. В результате работы веб-сервиса должна быть возвращена суммарная статистика по всем тэгам -
сколько раз встречался тег во всех вопросах и сколько раз на вопрос, содержащий тэг, был дан ответ.

5. Результат должен быть представлен в формате _JSON_.
Выдача ответа с человеко-читаемым форматированием _(pretty print)_ будет рассматриваться как плюс.
Пример ответа:
	```json
	{
	  "clojure": { "total": 73, "answered": 54},
	  "python": { "total": 10, "answered": 9},
	  "clojurescript": { "total": 19, "answered": 19}
	}
	```

**Использование сторонних компонентов**

Можно использовать любые open-source компоненты, доступные на maven central.
Можно использовать любое средство разработки веб-приложений, например
_akka-http_, _play framework_, сервлеты, _spring mvc_, _jax-rs_, и т.п. 

Приветствуются короткие решения, использующие сторонние компоненты.

**Ожидаемый результат**

Необходимо представить исходные тексты приложения.

Приложение:
1. Должно компилироваться и работать.

2. Должно собираться однострочной коммандой при помощи _sbt_, _maven_, или другого средства сборки,которое легко развернуть.

3. Должно запускаться одной коммандой. Например через `sbt run` или аналогичное средство, либо должны собираться в _runnable jar_.

4. Должно запускаться без необходимости отдельно разворачивать сервер приложений.

5. Должно работать за _proxy_.


Запуск сервера
-
Для запуска серевера можно использовать следующие шаги:
1.  Клонирование репозитория в локальную дирикторию:
	```bash
	git clone  https://github.com/orthodaos/tag-search-server.git
	```
2. Компиляция с сборка сервера. Из дириктории проекта выполнить команду:
	```bash
	sbt clean assembly
	```
	после этого в дириктории `bin/lib` должна появиться библиотека: `tag-search-server-1.0.jar`
3. Перед стартом сервера, при необходимости, можно изменить настройки в конфигурационном файле `tag-search-server.conf`
4. Запуск сервера осуществляется из дириктории `bin` запуском исполняемого скрипта `start-server`
	```bash
	./start-server
	```
	При этом в логи будут писаться в файл `bin/lib/task-scheduler.log`
4. Для остановки сервера запустить исполняемый скрипт `stop-server`, который появится после запуска:
	```bash
	./stop-server
	```


