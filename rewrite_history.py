import subprocess
import os

bot_name = "github-actions[bot]"

# The git filter-branch command
# We use skip_commit to remove commits by the bot
cmd = [
    'git', 'filter-branch', '--force', '--prune-empty',
    '--commit-filter',
    f'if [ "$GIT_AUTHOR_NAME" = "{bot_name}" ]; then skip_commit "$@"; else git commit-tree "$@"; fi',
    'HEAD'
]

print(f"Running command: {' '.join(cmd)}")
result = subprocess.run(cmd, capture_output=True, text=True)

print("STDOUT:")
print(result.stdout)
print("STDERR:")
print(result.stderr)
