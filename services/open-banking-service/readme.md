# Todo´s:

im account service folgendes iompleemntieren, folgende controller endupuntke:

- /balance
  (user kann die in der db gespüeicherten balance einsehen die sein konto hat)

- /accounts
  Alle gespeicherten Konten des Users (IBAN, Balance, Currency).

dementsprechend folgende todos:
- kafka account.update consumen
- informationen in db speichern
- ggf deduplizieren damit es keine doppelten inhalte gibt bevor in db gespeichert wird (externalId dafür nutzen)

