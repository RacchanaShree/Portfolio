@echo off
cd /d "c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\static"
copy /b index.html.part1 + index.html.part2 index.html
if exist index.html (
    echo [SUCCESS] index.html created.
    del index.html.part1 index.html.part2 merge.py test.txt
) else (
    echo [ERROR] index.html not found.
)
