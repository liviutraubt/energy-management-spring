# Sistem de Management al Energiei

Acest proiect este o aplicație web completă, construită pe o arhitectură de microservicii, destinată managementului utilizatorilor și al dispozitivelor de monitorizare a energiei. Aplicația este complet containerizată folosind Docker și orchestrată cu Docker Compose.

## Arhitectură

Sistemul este compus dintr-un frontend (React), un API Gateway (Traefik) și trei microservicii backend distincte (Spring Boot), fiecare cu propria sa bază de date PostgreSQL dedicată.

* **Frontend**: O aplicație React (CRA) servită printr-un container Nginx.
* **API Gateway**: Traefik gestionează toate cererile primite, acționând ca un reverse proxy. Acesta rutează traficul către serviciul corespunzător și utilizează `AuthenticationService` ca middleware pentru a valida token-urile JWT (Forward Authentication).
* **Backend (Microservicii)**:
    * **AuthenticationService**: Gestionează înregistrarea, autentificarea (login) și generarea/validarea token-urilor JWT.
    * **UserService**: Gestionează datele de profil ale utilizatorilor (nume, adresă, email etc.).
    * **DeviceService**: Gestionează dispozitivele (senzori, contoare) și asocierea acestora cu utilizatorii.
* **Baze de Date**: Fiecare microserviciu are propria sa bază de date PostgreSQL izolată, urmând principiul "o bază de date per serviciu".

---

## Serviciile Aplicației

| Serviciu | Tehnologie | Port Intern | Port Host (Expus) | Descriere |
| :--- | :--- | :--- | :--- | :--- |
| **Traefik (Gateway)** | Traefik | `80` | `81` | Punctul unic de intrare. Rutează traficul către servicii. |
| **Traefik Dashboard** | Traefik | `8080` | `8081` | Interfață web pentru monitorizarea Traefik. |
| **Frontend** | React / Nginx | `80` | (via Traefik `81`) | Interfața cu utilizatorul (Admin & Client). |
| **AuthenticationService**| Spring Boot | `8083` | (via Traefik `81`) | Gestionează conturile, rolurile și autentificarea (JWT). |
| **UserService** | Spring Boot | `8081` | (via Traefik `81`) | Gestionează datele de profil ale utilizatorilor (CRUD). |
| **DeviceService** | Spring Boot | `8082` | (via Traefik `81`) | Gestionează dispozitivele și maparea lor (CRUD). |
| **Auth DB** | PostgreSQL | `5432` | `5435` | Baza de date pentru `AuthenticationService`. |
| **User DB** | PostgreSQL | `5432` | `5433` | Baza de date pentru `UserService`. |
| **Device DB** | PostgreSQL | `5432` | `5434` | Baza de date pentru `DeviceService`. |

---

## Cerințe

* [Docker](https://www.docker.com/get-started)
* [Docker Compose](https://docs.docker.com/compose/install/) (de obicei inclus cu Docker Desktop)

---

## Instrucțiuni de Rulare

### 1. Crearea Rețelei Docker
Acest proiect necesită o rețea Docker externă numită `SD_Network` pentru a permite comunicarea între Traefik și containerele de servicii.

```bash
docker network create SD_Network
```

### 2. Pornirea Aplicației
Din directorul rădăcină al proiectului (unde se află `docker-compose.yml`), rulați comanda de mai jos. Aceasta va construi imaginile pentru fiecare serviciu și va porni toate containerele.

```bash
docker-compose up -d --build
```

Aplicația va fi acum pornită în modul "detached" (-d).

### 3. Oprirea Aplicației
Pentru a opri toate containerele, rulați:

```bash
docker-compose down
```

---

## Accesarea Aplicației

Odată ce toate containerele au pornit (poate dura 1-2 minute prima dată), sistemul este accesibil la următoarele adrese:

### Interfețe Principale
* **Aplicația Web (Frontend)**: [**http://localhost:81**](http://localhost:81)
* **Dashboard Traefik (Monitorizare)**: [**http://localhost:8081**](http://localhost:8081)

### Date de Logare (Admin)
Contul de administrator implicit este creat la prima pornire de către `AuthenticationService`:
* **Username**: `admin`
* **Parolă**: `admin`

### Documentație API (Swagger UI)
Fiecare microserviciu backend expune propria documentație API interactivă (Swagger UI), accesibilă prin gateway-ul Traefik:

* **Authentication Service**: [**http://localhost:81/api/auth/swagger-ui.html**](http://localhost:81/api/auth/swagger-ui.html)
* **User Service**: [**http://localhost:81/api/user/swagger-ui.html**](http://localhost:81/api/user/swagger-ui.html)
* **Device Service**: [**http://localhost:81/api/device/swagger-ui.html**](http://localhost:81/api/device/swagger-ui.html)

### Acces Direct la Bazele de Date (Opțional)
Bazele de date sunt expuse pe porturi diferite pe `localhost` pentru depanare facilă:

* **Baza de date Auth**: `localhost:5435`
* **Baza de date User**: `localhost:5433`
* **Baza de date Device**: `localhost:5434`

(Folosiți `postgres`, `root` și numele bazei de date corespunzătoare, conform `docker-compose.yml`, pentru a vă conecta cu un client PGAdmin sau similar).