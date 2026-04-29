# Tickets:
-> Transaction controller endpoints ausprobieren mit postman um zu prüfen ob alles klappt (ggf. erst unteres)
-> Api gateway nutzen um oberes ticket zu testen /\

-> Docker starten und ggf komplett neu mit infos füllen, sprich:
-> Geh von anfang an code durch und nochmal verstehen. Dabei laufen lassen und austesten

    

# Backlog:
- richtige userId generieren (im api gateway wahrscheinlich)
    - dann in transactions und category controller & service bearbeiten
- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)
- Auth Service erstellen
- Jwt aus Auth Service -> Signatur -> UserId => In api gateway implementieren bzw hinzufügen
- In transaction-service: Alerts publishen für notificationservice in kafka 
- Fehler beheben: wegen LocalDate musste ich extra KafkaConsumerConfig und ProducerCOnfig erstellen
  (ist deprecated), daher eine ebssere lösung finden
