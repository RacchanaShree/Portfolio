import os

src = r"c:\Users\THRIVENI GK\Desktop\Portfolio_web\index.html"
dst = r"c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\static\index.html"

try:
    if not os.path.exists(src):
        print(f"Source {src} not found!")
    else:
        with open(src, 'r', encoding='utf-8') as f:
            content = f.read()
        
        with open(dst, 'w', encoding='utf-8') as f:
            f.write(content)
        
        if os.path.exists(dst):
            print(f"Successfully wrote {len(content)} bytes to {dst}")
        else:
            print("Failed to write destination file.")
except Exception as e:
    print(f"Error: {e}")
