# Tickets:

- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)) -> GANZ ENTFERNEN
- Alle X-User-id stellen überprüfen und zu Long ändern und den uuid cast entfernen

# Backlog:
- In transaction-service: Alerts publishen für notificationservice in kafka 

- Fehler beheben: wegen LocalDate musste ich extra KafkaConsumerConfig und ProducerCOnfig erstellen
  (ist deprecated), daher eine ebssere lösung finden

- redirect url muss mit einer korrekten https url ausgetauscht werden
  - zurzeit benutzen wir ngrok dafür (application.properties (open-banking-servicer))

- In open-banking-service: echte jpa relation zwischen BankAccount und BankSession herstellen (OneToMany)

- Falls in Production gehen: alle secrets als env variable übergeben