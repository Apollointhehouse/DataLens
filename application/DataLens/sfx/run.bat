@echo off
if not "%1"=="min" start /min cmd /c %0 min & Exit /b
%~dp0\jre\bin\javaw.exe -jar %~dp0\app-1.0.jar
pause & exit