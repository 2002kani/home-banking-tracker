# Tickets:

- Api gateway soll funkjtionieren (bisher muss ich immer den port der einzelnen services
  mitgeben, ich kann nicht 8090 machen für api gateway)
- Dockerfile erstellen um alle services gleichzeitig laufen zu lassen, statt einzeln
- auth service erstellen

    

# Backlog:
- richtige userId generieren
    - dann in transactions und category controller & service bearbeiten
    - Jwt aus Auth Service -> Signatur -> UserId => In api gateway implementieren bzw hinzufügen
- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)
- Auth Service erstellen
- In transaction-service: Alerts publishen für notificationservice in kafka 
- Fehler beheben: wegen LocalDate musste ich extra KafkaConsumerConfig und ProducerCOnfig erstellen
  (ist deprecated), daher eine ebssere lösung finden
