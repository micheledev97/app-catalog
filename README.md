📦 CatalogoApp – README
🚀 Cos’è

Un’applicazione full-stack (Spring Boot + Angular) per gestire un catalogo prodotti.
Funzioni principali:

Login con JWT e ruoli (USER / ADMIN)

CRUD prodotti con filtri, paging e sort

Cache Redis per le performance 🚀

Carrello personalizzato per utente 🛒

🛠️ Requisiti

Java 17

PostgreSQL 14+

Redis 6+

Maven

Angular CLI

⚙️ Configurazione base

application.yml:

spring:
datasource:
url: jdbc:postgresql://localhost:5432/catalogo
username: catalogo
password: catalogo
jpa:
hibernate:
ddl-auto: update
data:
redis:
host: localhost
port: 6379

security:
jwt:
secret: CAMBIA_QUESTO
expiration: 3600000

🔑 Backend – punti forti

RedisConfig → JSON serializer per evitare errori di serializzazione

Controller → @PathVariable("id") obbligatorio

PageResponse → niente warning strani di PageImpl

Cache → @Cacheable e @CacheEvict(allEntries=true)

Carrello → salvato in Redis con chiave cart:{userId}

🎨 Frontend – chicche

Angular dev → ng serve → http://localhost:4200

Build prod → copiato in container Nginx

JWT salvato in localStorage

Interceptor → aggiunge sempre Authorization: Bearer <token>

🌐 Endpoint principali

Prodotti:

GET /products → lista

GET /products/{id} → dettaglio

POST /products → crea (solo ADMIN)

PUT /products/{id} → aggiorna (solo ADMIN)

DELETE /products/{id} → elimina (solo ADMIN)

Carrello:

GET /cart → vedi il tuo

POST /cart/{productId}?qty=2 → aggiungi

DELETE /cart/{productId} → rimuovi

DELETE /cart → svuota

▶️ Avvio

Backend:

mvn clean package
java -jar target/app.jar


Frontend (dev):

cd frontend
ng serve

🐞 Problemi comuni

Redis che si lamenta → usa JSON serializer

Parametri non trovati → ricordati @PathVariable("id")

Carrello condiviso → chiave per utente (cart:{userId})
