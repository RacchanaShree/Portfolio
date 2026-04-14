import subprocess
import os

bot_name = "github-actions[bot]"

# Try a simpler filter to test
cmd = [
    'git', 'filter-branch', '--force', '--index-filter',
    'git rm --cached --ignore-unmatch .github/last_ping.txt',
    'HEAD'
]

with open('rewrite_log.txt', 'w') as f:
    f.write(f"Running command: {' '.join(cmd)}\n")
    try:
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        f.write("STDOUT:\n")
        f.write(result.stdout)
        f.write("\nSTDERR:\n")
        f.write(result.stderr)
    except subprocess.CalledProcessError as e:
        f.write(f"Error: {e}\n")
        f.write(f"STDOUT: {e.stdout}\n")
        f.write(f"STDERR: {e.stderr}\n")
