import subprocess
import os

def run_git(args):
    try:
        result = subprocess.run(['git'] + args, capture_output=True, text=True, check=True)
        return result.stdout
    except subprocess.CalledProcessError as e:
        return f"Error: {e.stderr}"

print("Authors:")
print(run_git(['shortlog', '-sn', '--all']))

print("\nLog (last 10):")
print(run_git(['log', '-n', '10', '--oneline']))

print("\nBot Commits:")
print(run_git(['log', '--author=github-actions', '--oneline']))
