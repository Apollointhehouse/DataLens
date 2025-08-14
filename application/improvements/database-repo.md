#### Improvement:
  - I abstracted database operations away into a IndexRepo interface0
#### Why:
  - This change means I can use the object oriented programming concept of polymorphism in order to provide different implementations of a IndexRepo to my file searcher
#### Change:
  - In making this change and allowing better polymorphism, I was able to make different implementations of the IndexRepo, one such being FakeIndexRepo, this fake IndexRepo implementation allows me to better test the other components of my application without the uncertanty of IO/filesystem operations getting in the way
#### Evidence:
  - Commit 74c29de - "Improved error handling, extracted DB operations to FileIndexRepo interface (allows for swapping of DB operations impl which makes unit testing easier)", Commit 0b4495e - "Cleaned code, made it so result cards highlight on hover, etc"