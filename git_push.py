import subprocess
import os

def run_git_commands():
    project_dir = r"c:\Users\THRIVENI GK\Desktop\Portfolio_web"
    os.chdir(project_dir)
    
    commands = [
        ["git", "init"],
        ["git", "add", "."],
        ["git", "commit", "-m", "Initial commit of Portfolio Web Application"],
        ["git", "branch", "-M", "main"],
        ["git", "remote", "add", "origin", "https://github.com/RacchanaShree/Portfolio.git"],
        ["git", "push", "-u", "origin", "main"]
    ]
    
    for cmd in commands:
        print(f"Running: {' '.join(cmd)}")
        result = subprocess.run(cmd, capture_output=True, text=True)
        print(result.stdout)
        if result.stderr:
            print(f"Error: {result.stderr}")
        if result.returncode != 0:
            if "remote origin already exists" in result.stderr:
                print("Remote origin already exists, continuing...")
                continue
            break

if __name__ == "__main__":
    run_git_commands()
