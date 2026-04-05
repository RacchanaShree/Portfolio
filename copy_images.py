import shutil, os
src_dir = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web'
dst_dir = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\public'
for img in ['Rachu_proff.jpeg', 'Rachu_selfie.jpeg']:
    src = os.path.join(src_dir, img)
    dst = os.path.join(dst_dir, img)
    if os.path.exists(src):
        shutil.copy2(src, dst)
        print(f'OK: {img} -> {os.path.getsize(dst)} bytes')
    else:
        print(f'MISSING: {src}')
