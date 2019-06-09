# gatling-smtp

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