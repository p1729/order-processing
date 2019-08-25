# order-processing 
`multithreading-kata` `concurrency` `java8` 

Order events(Transaction)

|Transaction Id   | Order Id  | Version Id  | Symbol  | Quantity |Operation| Buy/Sell|
|-----------------|-----------|-------------|---------|----------|---------|---------|
|1                |1          |1            |ABC      |100       |INSERT   |BUY      |
|2|2|2|XYZ|10|UPDATE|SELL|
|3|2|1|XYZ|10|INSERT|SELL|
|4|1|3|ABC|10|CANCEL|BUY|



Process transaction(example given above) obeying following rules in a multithreaded environment
1. First version of order would always be INSERT
2. Last version of order would always be CANCEL
3. Order versions may come in any order
4. Process them in sequential order version 1,2,3....
5. Final position of every symbol will be calculated by difference between BUY and SELL units, CANCEL will mark order units 0
6. UPDATE operation may update the symbol, units, buy/sell

Output of the above example

|Symbol|Units|
|------|-----|
|XYZ   |-10  |
|ABC   |0    |
