# Todo´s:

wichtig: transactionId im transactiondto ist nicht gut da laut den docs es für ungeeignet ist für unique sachen
: dentification used for fetching transaction details.This value can not be used to uniquely identify transactions and may change if the list of transactions is retrieved again. Null if fetching transaction details is not supported.

daher nutze stattdessen eigenes hash.
- erstelle eigeen hash methode die einen unique key ausgibt
- den nutzt du dann und gibst ihn beim builden des events in schedulerservice an, statt tx.transactionI



## Todos:

- Balance bekommen in Scheduler Service wie mit trasnactions
- getTransactionsByDate erstellen und ihn dann in normalen cronjob rein packen statt getAllTransactions