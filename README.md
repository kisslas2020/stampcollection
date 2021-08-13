**Stamp Collection**

This is a program designed to register a collection of stamps.

It contains four models:

1. Stamp: The summary name of a stamp or stamps issued under one name. 
   Its fields are: id, name, country, yearOfIssue, set of denominations (@OneToMany)
   A new stamp can be saved by giving: name, country and yearOfIssue
2. Denomination: a denominations of a stamp.
   Its fields are: id, value, currency, stamp (@ManyToOne) and stock
   A new denomination can be saved by giving: value, currency (as string) and stampId
3. Transaction: by which we can buy or sell some denominations of stamps
   Its fields are: id, dateOfTransaction, transactionType (enum: BUY, SELL), list of items (@OneToMany)
   A new transaction can be saved by giving: dateOfTransaction and transactionType (as String)
4. Item: In one transaction we can buy different denominations of different stamps
   Its fields are: id, denomination (@ManyToOne), quantity, unitPrice, transaction (@ManyToOne)
   A new item can be saved by giving: denominationId, quantity, unitPrice and transactionId

All models have CRUD operations.

Restrictions:

* A stamp cannot be deleted while it has a denomination.
* A denomination cannot be deleted while it has positive stock value.
* A transaction cannot be deleted while it has items.
* Only the last item can be deleted.

* We cannot sell more than we have.

Field validations:
* Stamp: name: cannot be blank
         country: use three-letter country codes, cannot be blank
         yearOfIssue: integer between 1840 and current year
* Denomination: value: cannot be null, must be positive
                currency: use three-letter currency codes, cannot be null
                stampId: cannot be null
* Transaction: dateOfTransaction: cannot be null
               transactionType: cannot be null, only BUY or SELL (case-sensitive)
* Item: denominationId: cannot be null
        quantity: cannot be null, must be positive
        unitPrice: cannot be null, must be positive
        transactionId: cannot be null
