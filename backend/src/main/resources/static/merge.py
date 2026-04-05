import os

base_path = r'c:\Users\THRIVENI GK\Desktop\Portfolio_web\backend\src\main\resources\static'
parts = ['index.html.part1', 'index.html.part2']
output_file = os.path.join(base_path, 'index.html')

with open(output_file, 'wb') as outfile:
    for part in parts:
        part_path = os.path.join(base_path, part)
        with open(part_path, 'rb') as infile:
            outfile.write(infile.read())

print(f"Successfully created {output_file}")
