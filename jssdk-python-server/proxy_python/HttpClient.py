#!/usr/bin/python
# encoding=utf-8
import hashlib
import time
import uuid

import requests

from proxy_python.RequestMethod import RequestMethod
from proxy_python.SdkException import HttpException, BaseSdkException


class HttpClient:
    def __init__(self, serverUrl: object, accessToken: object, accessSecret: object) -> object:
        self.__serverUrl = serverUrl
        self.__accessToken = accessToken
        self.__accessSecret = accessSecret

    def request(self, requestData):
        '''
        post请求
        :param url: 请求地址
        :param data: 请求数据
        :param headers: 请求header
        :return: 返回数据
        '''

        method = requestData.getApiMethod()
        data = requestData.getApiParams()
        url = self.__serverUrl + requestData.getApiUrl()

        global toMd5
        nonce = uuid.uuid1()
        timestamp = str(int(time.time() * 1000))
        headers = {'x-qys-open-signature': toMd5(self.__accessToken + self.__accessSecret + timestamp + str(nonce)),
                   'x-qys-open-accesstoken': self.__accessToken,
                   'x-qys-open-timestamp': timestamp,
                   'x-qys-open-nonce': str(nonce),
                   'version': "PROXY-PYTHON"}

        '''
        请求失败后重试2次
        '''
        failNum = 0
        retry = 2
        while (True):
            if (method == RequestMethod.POST):
                files = requestData.getFileParams()
                response = requests.post(url, data=data, headers=headers, files=files)
            elif (method == RequestMethod.GET):
                response = requests.get(url, params=data, headers=headers)
            else:
                raise BaseSdkException(message="request method is invalid")

            if (response.status_code == 200):
                return response.content
            else:
                if (failNum == retry):
                    raise HttpException(response.status_code, response.reason)
                failNum = failNum + 1


def toMd5(plainText):
    '''
    md5加密
    :param plainText: 要加密的字符串
    :return:
    '''

    md5 = hashlib.md5()
    md5.update(plainText.encode('utf-8'))
    return md5.hexdigest()
