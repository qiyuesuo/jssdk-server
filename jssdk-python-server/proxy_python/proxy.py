from django.http import HttpResponse

from proxy_python.BaseRequest import BaseRequest
from proxy_python.HttpClient import HttpClient
from proxy_python.RequestMethod import RequestMethod


def do_proxy(request):
    proxy_client = HttpClient('https://openapi.qiyuesuo.cn', '替换开放平台App Token', '替换开放平台App Secret')
    if request.method == 'POST':
        post_data = request.POST
        request_data = post_data.get('requestData', default=None)
        post_files = request.FILES
        file_array = post_files.get('file', default=None)
        proxy_data = BaseRequest(RequestMethod.POST)
        proxy_data.addApiParam('requestData', request_data)
        if file_array is not None:
            proxy_data.addFileParam('file', file_array)
    elif request.method == 'GET':
        get_data = request.GET
        request_data = get_data.get('requestData', default=None)
        proxy_data = BaseRequest(RequestMethod.GET)
        proxy_data.addApiParam('requestData', request_data)
    response = proxy_client.request(proxy_data)
    return HttpResponse(response)
