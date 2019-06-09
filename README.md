# gatling-smtp

## DSL
```scala
import camaral.gatling.smtp.Predef._
import io.gatling.core.Predef._

val smtpProtocol = smtp
    .host("localhost")
    .port(1025)

val scn = scenario("Simple scenario")
    .exec(smtp("My First Request")
      .from("edson.pele@example.com")
      .to("diego.maradona@example.com")
      .subject("Best Player?")
      .body("Pele > Maradona"))
```

## Testing the project
- Run a SMTP server to print the request:
```
python -m smtpd -n -c DebuggingServer localhost:1025
```

- Enter on sbt shell:
```
./sbt
```

- Run tests:
```
gatling:test
```