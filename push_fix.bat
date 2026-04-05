@echo off
git init
git add .
git commit -m "Initial commit of Portfolio Web Application"
git branch -M main
git remote add origin "https://github.com/RacchanaShree/Portfolio.git"
git push -f origin main
