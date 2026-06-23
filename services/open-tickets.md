# Tickets:


# Backlog:
- Man soll sein spar ziel selbst setzen können (welcher angezeigt wird in dashboard) 

- In transaction-service: Alerts publishen für notificationservice in kafka 

- Fehler beheben: wegen LocalDate musste ich extra KafkaConsumerConfig und ProducerCOnfig erstellen
  (ist deprecated), daher eine ebssere lösung finden

- redirect url muss mit einer korrekten https url ausgetauscht werden
  - zurzeit benutzen wir ngrok dafür (application.properties (open-banking-servicer))

- In open-banking-service: echte jpa relation zwischen BankAccount und BankSession herstellen (OneToMany)

- Falls in Production gehen: alle secrets als env variable übergeben

- Effizientere suche bei getBanks() ggf. dass man suchen kann oder weiter eingrenzen

- openapi swagger ui schöner gestalten