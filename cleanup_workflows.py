import os
from pathlib import Path

base_path = r"c:\Users\THRIVENI GK\Desktop\Portfolio_web"
paths = [
    os.path.join(base_path, ".github", "workflows", "keep-awake.yml"),
    os.path.join(base_path, ".github", "workflows", "keep-alive.yml")
]

print(f"Current Working Directory: {os.getcwd()}")

for path in paths:
    p = Path(path)
    if p.exists():
        print(f"Attempting to delete {path}...")
        try:
            os.remove(path)
            if not p.exists():
                print(f"Successfully deleted {path}")
            else:
                print(f"CRITICAL ERROR: File still exists after deletion attempt: {path}")
        except Exception as e:
            print(f"FAILED to delete {path}: {str(e)}")
    else:
        print(f"Path does not exist: {path}")
