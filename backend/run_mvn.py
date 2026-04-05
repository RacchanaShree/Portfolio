import subprocess
result = subprocess.run(['cmd', '/c', 'mvnw.cmd', 'clean', 'compile'], cwd=r'C:\Users\THRIVENI GK\OneDrive\Desktop\Portfolio_web\backend', capture_output=True, text=True)
print("STDOUT:")
print(result.stdout)
print("STDERR:")
print(result.stderr)
