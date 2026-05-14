# Tickets:
- Api gateway soll funkjtionieren (zurzeit 404 error)

    

# Backlog:
- richtige userId generieren
    - dann in transactions und category controller & service bearbeiten
    - Jwt aus Auth Service -> Signatur -> UserId => In api gateway implementieren bzw hinzufügen

- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)

- In transaction-service: Alerts publishen für notificationservice in kafka 

- Fehler beheben: wegen LocalDate musste ich extra KafkaConsumerConfig und ProducerCOnfig erstellen
  (ist deprecated), daher eine ebssere lösung finden

- redirect url muss mit einer korrekten https url ausgetauscht werden
  - zurzeit benutzen wir ngrok dafür (application.properties (open-banking-servicer))

- In open-banking-service: echte jpa relation zwischen BankAccount und BankSession herstellen (OneToMany)