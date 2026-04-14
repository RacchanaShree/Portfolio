import subprocess

def run_git(args):
    result = subprocess.run(['git'] + args, capture_output=True, text=True)
    return result.stdout

print(run_git(['shortlog', '-sn', '--all']))
