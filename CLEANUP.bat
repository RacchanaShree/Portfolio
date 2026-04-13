@echo off
echo ========================================
echo   PORTFOLIO CLEANUP - Removing Backend
echo ========================================
echo.

echo [1/4] Removing backend folder...
rmdir /s /q backend 2>nul
if exist backend (
    echo   WARNING: backend folder still locked. Close VS Code and retry.
) else (
    echo   OK: backend deleted.
)

echo [2/4] Removing data folder...
rmdir /s /q data 2>nul
if exist data (
    echo   WARNING: data folder still locked. Close VS Code and retry.
) else (
    echo   OK: data deleted.
)

echo [3/4] Removing legacy scripts...
del /f /q move_static.bat 2>nul
del /f /q del_all.bat 2>nul
del /f /q push_fix.bat 2>nul
del /f /q push_to_github.bat 2>nul
del /f /q update_html.py 2>nul
del /f /q cleanup_workflows.py 2>nul
del /f /q copy_images.py 2>nul
del /f /q fetch_error.py 2>nul
del /f /q git_push.py 2>nul
del /f /q migrate_index.py 2>nul
del /f /q final_move.py 2>nul
del /f /q merge_static.py 2>nul
del /f /q temp_cleanup.bat 2>nul
echo   OK: Legacy scripts removed.

echo [4/4] Removing GitHub workflow files...
del /f /q ".github\workflows\keep-alive.yml" 2>nul
del /f /q ".github\workflows\keep-awake.yml" 2>nul
del /f /q ".github\last_ping.txt" 2>nul
echo   OK: Workflow files removed.

echo.
echo ========================================
echo   CLEANUP COMPLETE! Remaining files:
echo ========================================
dir /b
echo.
echo ========================================
echo   Now deleting this cleanup script...
echo ========================================
(goto) 2>nul & del "%~f0"
