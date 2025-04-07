# CurrencyHub - Сервис работы с курсами валют

# Назначение сервиса

CurrencyHub - это микросервис для обработки и расчета курсов валют, который:

- Автоматически получает актуальные курсы валют от Центробанка России
- Вычисляет кросс-курсы для произвольных валютных пар
- Обеспечивает надежное расписание обновления данных с механизмами блокировки

# Ключевые функции

1. Получение данных о курсах валют
    - Ежедневное автоматическое обновление курсов через API ЦБ РФ
    - Парсинг XML-ответа в структурированный формат
    - Поддержка 43 валют (доллар, евро, юань и др.)
2. Расчет кросс-курсов
    - Поддержка любых комбинаций валютных пар
3. Распределенное планирование задач
    - Ежесуточное обновление в 10:00 по МСК
    - Гарантия однократного выполнения благодаря SchedulerLock
    - Настройка временных интервалов блокировки (min/max duration)

# Технологический стек
   - Язык: Java 17+
   - Фреймворки: Spring Boot 3.x, Spring Web MVC, Spring Data JPA, Spring Scheduler
   - Блокировки: ShedLock 6.3.1 (с JDBC-провайдером)
   - База данных: PostgreSQL (+ Flyway для миграций)
   - Формат данных: XML (вход), JSON (выход)
   - Маппинг: MapStruct 1.6.3
   - Сериализация: Jackson (XML через JAXB + JSON)
   - Парсинг XML: Jakarta XML Binding (JAXB) 4.0.2
   - Тестирование: Testcontainers (PostgreSQL, MockServer), MockServer 5.15.0, REST Assured