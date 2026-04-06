import urllib.request
import urllib.error

url = 'https://portfolio-1-rd3h.onrender.com/api/skills'
try:
    with urllib.request.urlopen(url) as response:
        with open('test.txt', 'w') as f:
            f.write(response.read().decode('utf-8'))
except urllib.error.HTTPError as e:
    with open('test.txt', 'w') as f:
        f.write(f"HTTP Error {e.code}: {e.read().decode('utf-8')}")
except Exception as e:
    with open('test.txt', 'w') as f:
        f.write(str(e))
