import subprocess
import os

def run(cmd):
    res = subprocess.run(cmd, capture_output=True, text=True, shell=True)
    return res.stdout + res.stderr

with open('git_status_debug.txt', 'w') as f:
    f.write("STATUS:\n")
    f.write(run('git status'))
    f.write("\n\nLOG:\n")
    f.write(run('git log -n 5 --oneline'))
