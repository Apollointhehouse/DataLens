@echo off

powershell "$s=(New-Object -COM WScript.Shell).CreateShortcut(\"%userprofile%\Desktop\DataLens.lnk\");$s.TargetPath=\"C:\\Users\\%USERNAME%\\DataLens\\run.bat\";$s.Save()"