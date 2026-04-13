import subprocess
import os

def run_git_commands():
    project_dir = r"c:\Users\THRIVENI GK\Desktop\Portfolio_web"
    os.chdir(project_dir)
    
    commands = [
        ["git", "add", "."],
        ["git", "commit", "-m", "Fix resume download logic and remove featured badges"],
        ["git", "push", "origin", "main"]
    ]
    
    for cmd in commands:
        print(f"Running: {' '.join(cmd)}")
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, check=True)
            print(result.stdout)
            if result.stderr:
                print(f"Error: {result.stderr}")
        except subprocess.CalledProcessError as e:
            print(f"Failed to run {' '.join(cmd)}")
            print(f"Exit code: {e.returncode}")
            print(f"Stdout: {e.stdout}")
            print(f"Stderr: {e.stderr}")
            break

if __name__ == "__main__":
    run_git_commands()
