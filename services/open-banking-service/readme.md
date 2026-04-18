# Todo´s:

im account service folgendes implementieren, folgende controller endpunkte:

- /balance
  (user kann die in der db gespüeicherten balance einsehen die sein konto hat)

- /accounts
  Alle gespeicherten Konten des Users (IBAN, Balance, Currency).


TODOS:

1. Folgendes soll funktionieren: nach erfolgreicher verbindung mit bank konto innerhalb authAndSave() soll ein event gepublished werden
der die entity acount in account service entspricht, und dieser soll einmnalig dann genutzt werden um in die datenbank zu speichern.

3. updateAccount fertig stellen -> soll balance immer nach consume ändern

4. die oberen endpunkte implementieren samt logik
 


# Langzeit Todos:
- richtige userId generieren (im api gateway wahrscheinlich)
- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)
