# db-rgr

db-rgr - расчетно-графическая работа по дисциплине "Базы данных" в Тверском государственном университете

## Запуск

### Из скомпилированного jar

1. Установить Java 17
2. [Скачать jar](https://github.com/6rayWa1cher/db-rgr/releases/download/release/db-rgr-1.0.0.jar)
3. Сделать рядом со скачанным jar файл `config.yml`, заполнив его по шаблону:
   ```yml
   db:
     jdbc: jdbc:postgresql://localhost:5432/dbrgr
     user: postgres
     password: postgres
   ```
4. `$ java -jar db-rgr-1.0.0.jar`

### Из исходников

1. Установить Java 17 и Maven
2. Скачать проект
3. Скопировать файл `src/main/resources/config-template.yml` в `src/main/resources/config.yml`, вставив нужные данные
4. `$ mvn clean package` в корне проекта
5. `$ java -jar target/db-rgr-1.0.0-jar-with-dependencies.jar` в корне проекта