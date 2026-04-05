@echo off
set "src=c:\Users\THRIVENI GK\Desktop\Portfolio_web\index.html"
set "dest_dir=c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\public"
set "dest_file=%dest_dir%\index.html"

if not exist "%dest_dir%" mkdir "%dest_dir%"
echo [LOG] Created directory %dest_dir%

copy /y "%src%" "%dest_file%"
if exist "%dest_file%" (
    echo [SUCCESS] File moved to %dest_file%
    for %%I in ("%dest_file%") do echo [SIZE] %%~zI bytes
) else (
    echo [ERROR] Failed to move file!
)
