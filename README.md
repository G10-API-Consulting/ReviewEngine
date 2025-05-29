

## 1. Översikt

ReviewEngine är en Java Spring Boot-applikation som genererar och hanterar produktrecensioner. Den använder AI (Claude 3 Haiku) för att skapa recensioner och hämtar väderdata från OpenWeatherMap för att påverka recensionernas ton. Applikationen använder JWT för autentisering och har stöd för API-nyckel-skyddade endpoints.

## 2. Kom igång
Förutsättningar för att köra projektet:
1. Java 21
2. Maven
3. MySQL-databas
4. OpenWeatherMap API-nyckel
5. Anthropic api key

## 3. Installation
1. Klona projektet från GitHub:
https://github.com/G10-API-Consulting/ReviewEngine.git

2. Skapa en .env-fil i projekt-mappen med följande innehåll:
``` javascrip
WEATHER_API_KEY=din_weather_api_nyckel
DB_HOST=localhost
DB_PORT=3306
DB_NAME=reviewengine
DB_USER=användare
DB_PASSWORD=lösenord
SPRING_PROFILES_ACTIVE=dev
```

4. Starta MySQL-databasen (exempel med Docker):
``` javascript
docker run --name reviewengine-mysql -e MYSQL_ROOT_PASSWORD=lösenord -e MYSQL_DATABASE=reviewengine -p 3306:3306 -d mysql:8
```
## 4. Kör Applikationen
Bygg och kör applikationen, sedan starta upp din webläsare och skriv in följande adress för att använda API:et med Swagger:
http://localhost:8080/swagger-ui/index.html

## 5. Inloggning och Autensiering i Swagger
1. Gå till AuthControllern, specifikt till endpointen /register. Tryck på "Try it out" och fyll i namn, användarnamn och password i JSON formatet du ser. Tryck sen "Execute".

2. Gå vidare till /login endpointen. Tryck på "Try it out", fyll i med ditt valda username och password. Tryck "Execute", I respons-bodyn så borde du se en JWT token, kopiera den.

3. Uppe i högra hörnet på Swagger så finns ett hänglås. Tryck på den, i det översta fältet "bearerAuth" så kan du klistra in din JWT-token och tryck på "Authorize".

4. Gå till api-key-controller, öppna /generate endpointen och tryck på "Try it out". Tryck sedan på "Execute", i respons-bodyn så ska du ha fått en API-Key. Kopiera den och spara undan den.

5. Gå upp till hänglåset igen från steg 3. Fyll i det nedersta fältet "apiKeyAuth" med din API-nyckel. Tryck sen på "Authorize".

6. Nu när du har din API-nyckel så räcker det med att autensiera med den för att använda alla endpoints i API:et.
