#### Improvement:
  - I moved from storing search indexes in a HashMap in memory to a sqlite DB
#### Why: 
  - Storing 1000's of search indexes in memory uses a significant amount of memory, and may make the program less suitable for users on lower end devices, storing the indexes in memory also means that if the program is to close and repoen, the indexs must be regenerated as they have been lost
#### Change: 
  - After changing to storing the search indexes in a light-weight sqlite DB instead of memory, I saw an immediate drop in memory consumption with only a slight (unnoticible to the user) increase in search time, also, after switching to storing the search indexes in a sqlite database, the indexes would now be saved even if the program was closed and reopened, this means the program could be potentially saving 2-5min of runtime every single time the program is opened.
#### Evidence:
  - Commit dac0039 - "Improved error handling, save search indexes to sqlite DB instead of HashMap (saves on memory usage)"