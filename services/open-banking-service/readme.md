# Todo´s:

im account service folgendes implementieren, folgende controller endpunkte:

- /balance
  (user kann die in der db gespüeicherten balance einsehen die sein konto hat)

- /accounts
  Alle gespeicherten Konten des Users (IBAN, Balance, Currency).


TODOS:
- db löschen und nochmal neu auth durhcgehen und prüfen ob alles passt (keine fehler? userid mitgegeben? balance vernünftig geändert? Account table erstellt?)
- die oberen endpunkte implementieren samt logik
 


# Langzeit Todos:
- richtige userId generieren (im api gateway wahrscheinlich)
- hardcoded values mit solider logik austauschen (OpenBankingController, line:53)
