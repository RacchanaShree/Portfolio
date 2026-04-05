import os

# Absolute paths for maximum reliability
base = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web'
dest_dir = os.path.join(base, 'backend', 'src', 'main', 'resources', 'public')
output = os.path.join(dest_dir, 'index.html')
parts = ['index.html.part1', 'index.html.part2', 'index.html.part3']

if not os.path.exists(dest_dir):
    os.makedirs(dest_dir)
    print(f"Created {dest_dir}")

with open(output, 'wb') as outfile:
    for p in parts:
        part_path = os.path.join(dest_dir, p)
        if os.path.exists(part_path):
            with open(part_path, 'rb') as infile:
                outfile.write(infile.read())
            print(f"Merged {part_path}")
        else:
            print(f"MISSING: {part_path}")

print(f"Final size: {os.path.getsize(output)} bytes")
