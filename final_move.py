import os
import shutil

source = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web\index.html'
dest_dir = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\public'
dest_file = os.path.join(dest_dir, 'index.html')

try:
    if not os.path.exists(dest_dir):
        os.makedirs(dest_dir)
        print(f"Created directory: {dest_dir}")
    
    shutil.copy2(source, dest_file)
    size = os.path.getsize(dest_file)
    print(f"Successfully copied {source} to {dest_file}")
    print(f"Final file size: {size} bytes")
    
    # Optional cleanup of root moved file
    # os.remove(source) 

except Exception as e:
    print(f"CRITICAL ERROR: {str(e)}")
