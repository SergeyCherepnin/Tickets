# Tickets
______________________________________________________________________________________________________________________________________
## Задание:
Напишите программу на языке программирования
java, которая прочитает файл tickets.json и
рассчитает:
- среднее время полета между городами Владивосток
и Тель-Авив
- 90-й процентиль времени полета между городами
Владивосток и Тель-Авив
Программа должна вызываться из командной строки
Linux, результаты должны быть представлены в
текстовом виде.

## Использованные технологии:
- *Java 11*
- *Maven*
- *Lombok*
- *Jackson*

## Решение:
Для запуска программы нужно:
- клонировать репозиторий `git clone https://github.com/SergeyCherepnin/Tickets`
- собрать проект, перейдя в корневую папку проекта `mvn clean package`
- запустить сгенерированный jar-файл `java -jar target/Tickets-1.0-SNAPSHOT-shaded.jar files/tickets.json files/result.txt`

Программа читает данные из аргументов командной строки *files/tickets.json*, записывает данные в файл *files/result.txt*

Чтобы использовать другие источники данных в аргументах командной строки введите свои данные, где первый аргумент - источник данных, второй - файл для записи результата.

* среднее время полета между городами Владивосток и Тель-Авив: 10ч 32мин 00сек
* 90-й процентиль времени полета между городами Владивосток и Тель-Авив: 12ч 45мин 00сек
