import json
import pycurl
from StringIO import StringIO

# thou should change the path
file_path = '/Users/oldfatcrab/lang/parser/Common/src/main/resources/courseDef/courseDef.json'
temp = 'https://api.uwaterloo.ca/v2/courses/%s/%s.json?key=4dedfc23e8a9727ac98e62a495a20ac0'
f = open(file_path, 'r')
course_list = json.loads(f.read())
f.close()
for course in course_list:
    buf = StringIO()
    c = pycurl.Curl()
    url = str((temp % (course['subject'], course['catalog_number'])).encode('utf-8'))
    c.setopt(c.URL, url)
    c.setopt(c.WRITEFUNCTION, buf.write)
    c.perform()
    c.close()
    course_info = json.loads(buf.getvalue())
    anti_str = course_info['data']['antirequisites']
    if anti_str:
        print course['subject'], course['catalog_number'], "::", anti_str

