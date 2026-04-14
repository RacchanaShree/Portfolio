import os

files = [
    r'.github\workflows\keep-alive.yml',
    r'.github\workflows\keep-awake.yml',
    r'.github\last_ping.txt'
]

for file in files:
    full_path = os.path.join(os.getcwd(), file)
    if os.path.exists(full_path):
        try:
            os.remove(full_path)
            print(f"Deleted {file}")
        except Exception as e:
            print(f"Error deleting {file}: {e}")
    else:
        print(f"{file} does not exist")
