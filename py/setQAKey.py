import requests
from lxml import html

#curl -u "adminteam1:3h@VsX" -d "encryptionType=1&keychain=3ab2125e5d1fdc3f07089c8e89da50fa&keyhole=1adbd866553f6f4d010f401cf75ff3d9" -k 10.119.33.26:8081/msadmin/setMultipleWrapKey.jsp;
#curl -u "adminteam2:hX@Wo7" -d "encryptionType=1&keychain=tobeorwaitqaornottoqaorsomethinglikethat&keyhole=ac144bc570d07acac8f318a190a16972f7427e9c9b33deed" -k 10.119.33.26:8081/msadmin/setMultipleAESKey.jsp;
#curl -u "adminteam1:3h@VsX" -d "encryptionType=14&keychain=fea5c1309e990135803e888fcd584242&keyhole=f1aaddee758a0ec892397cdfd7e3e6e3" -k 10.119.33.26:8081/msadmin/setMultipleWrapKey.jsp
#curl -u "adminteam2:hX@Wo7" -d "encryptionType=14&keychain=hellohello&keyhole=7597fd11c08341a15942a680570c82614dfe018a19ccb7d0" -k 10.119.33.26:8081/msadmin/setMultipleAESKey.jsp

WRAPKEY_URL = "http://10.119.33.26:8081/msadmin/setMultipleWrapKey.jsp"
WRAPKEY_TEAM = "adminteam1"
WRAPKEY_PASS = "3h@VsX"
WRAPKEY_BASIC_AUTH = "Basic YWRtaW50ZWFtMTozaEBWc1g="
#WRAPKEY1 = "encryptionType=1&keychain=3ab2125e5d1fdc3f07089c8e89da50fa&keyhole=1adbd866553f6f4d010f401cf75ff3d9"
WRAPKEY1 = {'encryptionType':'1', 'keychain':'3ab2125e5d1fdc3f07089c8e89da50fa','keyhole':'1adbd866553f6f4d010f401cf75ff3d9'}
#WRAPKEY14 = "encryptionType=14&keychain=fea5c1309e990135803e888fcd584242&keyhole=f1aaddee758a0ec892397cdfd7e3e6e3"
WRAPKEY14 = {'encryptionType':'14','keychain':'fea5c1309e990135803e888fcd584242','keyhole':'f1aaddee758a0ec892397cdfd7e3e6e3'}


#Set-Cookie:JSESSIONID=d7cvafnkcnpu1kp9mjghvjn44;Path=/msadmin
#Set-Cookie:WrapKey.TOKEN=eea79da16211b8c0cde9d1d60b991e62

AESKEY_URL = "http://10.119.33.26:8081/msadmin/setMultipleAESKey.jsp"
AESKEY_TEAM = "adminteam2"
AESKEY_PASS = "hX@Wo7"
AESKEY_BASIC_AUTH = "Basic YWRtaW50ZWFtMjpoWEBXbzc="
#AESKEY1 = "encryptionType=1&keychain=tobeorwaitqaornottoqaorsomethinglikethat&keyhole=ac144bc570d07acac8f318a190a16972f7427e9c9b33deed"
AESKEY1 = {'encryptionType':'1','keychain':'tobeorwaitqaornottoqaorsomethinglikethat','keyhole':'ac144bc570d07acac8f318a190a16972f7427e9c9b33deed'}
#AESKEY14 = "encryptionType=14&keychain=hellohello&keyhole=7597fd11c08341a15942a680570c82614dfe018a19ccb7d0"
AESKEY14 = {'encryptionType':'14','keychain':'hellohello','keyhole':'7597fd11c08341a15942a680570c82614dfe018a19ccb7d0'}


#USERNAME = "nfeng"
#PASSWORD = "password"

#LOGIN_URL = "https://gitlab.dev.activenetwork.com/users/sign_in"
#LOGIN_REST = "https://gitlab.dev.activenetwork.com/users/auth/ldapmain/callback"
#URL = "https://gitlab.dev.activenetwork.com/dashboard/projects"

def setKey(url, team, password, data):
    session_requests = requests.session()
    r = session_requests.get(url, auth=(team, password))
    
    #set_cookie = r.headers['Set-Cookie']
    #content_type = r.headers['Content-Type']
    #headers = {'Set-Cookie' : set_cookie, 'Content-Type' : content_type}
    #print(headers)

    print(r.cookies)
    JSESSIONID = r.cookies['JSESSIONID']
    print(JSESSIONID)
    KEY_CSRF_TOKEN = r.cookies['KEY.CSRF.TOKEN']
    print(KEY_CSRF_TOKEN)
    headers = {'Cookie':'KEY.CSRF.TOKEN='+KEY_CSRF_TOKEN+'; JSESSIONID='+JSESSIONID,'Content-Type':'application/x-www-form-urlencoded','Connection':'keep-alive'}
    print(headers)
    r = session_requests.post(url, auth=(team, password), headers=headers, data=data)
 
    print(r.cookies)
    JSESSIONID = r.cookies['JSESSIONID']
    print(JSESSIONID)
    KEY_CSRF_TOKEN = r.cookies['KEY.CSRF.TOKEN']
    print(KEY_CSRF_TOKEN)
    headers = {'Cookie':'KEY.CSRF.TOKEN='+KEY_CSRF_TOKEN+'; JSESSIONID='+JSESSIONID,'Content-Type':'application/x-www-form-urlencoded','Connection':'keep-alive'}
    print(headers)
    r = session_requests.post(url, auth=(team, password), headers=headers, data=data)


    print(r.headers)
    print(r.status_code)
    print(r.text)
    pass


def main():
    #session_requests = requests.session()
    #r = session_requests.get(WRAPKEY_URL, auth=(WRAPKEY_TEAM, WRAPKEY_PASS))
    #cookie = r.headers['Set-Cookie']
    #content_type = r.headers['Content-Type']
    #headers = {'Set-Cookie' : cookie, 'Content-Type' : content_type}
    #print(headers)
    #r = session_requests.post(WRAPKEY_URL, auth=(WRAPKEY_TEAM, WRAPKEY_PASS), headers=headers, data=WRAPKEY1)
    #print(r.status_code)

    setKey(WRAPKEY_URL, WRAPKEY_TEAM, WRAPKEY_PASS, WRAPKEY1)
    setKey(AESKEY_URL, AESKEY_TEAM, AESKEY_PASS, AESKEY1)

    setKey(WRAPKEY_URL, WRAPKEY_TEAM, WRAPKEY_PASS, WRAPKEY14)
    setKey(AESKEY_URL, AESKEY_TEAM, AESKEY_PASS, AESKEY14)







    #print(r.status_code)
    #print(r.headers['JSESSIONID']) wrong
    #print(r.headers['WrapKey.TOKEN']) wrong
    #print(r.headers['content-type'])
    #print(r.headers)
    #print(r.headers['Set-Cookie'])

    # Get login csrf token
    #result = session_requests.get(LOGIN_URL)
    #tree = html.fromstring(result.text)
    #authenticity_token = list(set(tree.xpath("//input[@name='authenticity_token']/@value")))[0]
    #print(authenticity_token)

    # Create payload
    #payload = {
    #    "username": USERNAME,
    #    "password": PASSWORD,
    #    "remember_me": "1",
    #    "authenticity_token": authenticity_token
    #}

    # Perform login
    #result = session_requests.post(LOGIN_REST, data = payload, headers = dict(referer = LOGIN_URL))

    # Scrape url
    #result = session_requests.get(URL, headers = dict(referer = URL))
    #tree = html.fromstring(result.content)
    #print(result.text)
    #bucket_names = tree.xpath("//a[@class='project']/@href")

    #print(bucket_names)

if __name__ == '__main__':
    main()