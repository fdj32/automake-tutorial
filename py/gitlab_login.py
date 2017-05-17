import requests
from lxml import html

USERNAME = "nfeng"
PASSWORD = "password"

LOGIN_URL = "https://gitlab.dev.activenetwork.com/users/sign_in"
LOGIN_REST = "https://gitlab.dev.activenetwork.com/users/auth/ldapmain/callback"
URL = "https://gitlab.dev.activenetwork.com/dashboard/projects"

def main():
    session_requests = requests.session()

    # Get login csrf token
    result = session_requests.get(LOGIN_URL)
    tree = html.fromstring(result.text)
    authenticity_token = list(set(tree.xpath("//input[@name='authenticity_token']/@value")))[0]
    #print(authenticity_token)

    # Create payload
    payload = {
        "username": USERNAME,
        "password": PASSWORD,
        "remember_me": "1",
        "authenticity_token": authenticity_token
    }

    # Perform login
    result = session_requests.post(LOGIN_REST, data = payload, headers = dict(referer = LOGIN_URL))

    # Scrape url
    result = session_requests.get(URL, headers = dict(referer = URL))
    tree = html.fromstring(result.content)
    #print(result.text)
    bucket_names = tree.xpath("//a[@class='project']/@href")

    print(bucket_names)

if __name__ == '__main__':
    main()