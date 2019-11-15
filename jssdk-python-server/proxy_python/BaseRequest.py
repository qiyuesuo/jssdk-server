#!/usr/bin/python
# encoding=utf-8

class BaseRequest:

    def __init__(self, method):
        self.__apiUrl = '/uniformapi'
        self.__apiMethod = method
        self.__apiParams = {}
        self.__fileParams = {}

    def getApiUrl(self):
        return self.__apiUrl

    def getApiMethod(self):
        return self.__apiMethod

    def addApiParam(self, key, value):
        self.__apiParams[key] = value

    def getApiParams(self):
        return self.__apiParams

    def addFileParam(self, key, value):
        self.__fileParams[key] = value

    def setFileParam(self, files):
        self.__fileParams = files

    def getFileParams(self):
        return self.__fileParams
