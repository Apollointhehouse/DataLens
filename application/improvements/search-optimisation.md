#### Improvement:
  - Indexed search paths for file system
#### Why:
  - This prevents the program from having to rescan the entire file system every time a file is searched
#### Change:
  - Upon implementing this change, search times went from ~15min per search to ~77ms per search
#### Evidence:
  - Commit 6e1b453 - "Index searched paths as optimisation"