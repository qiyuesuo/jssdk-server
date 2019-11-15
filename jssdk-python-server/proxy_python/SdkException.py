#!/usr/bin/python
# encoding=utf-8


class BaseSdkException(RuntimeError):

    def __init__(self, code=100, message='ERROR'):
        self.__code = code
        self.__message = message

    def getCode(self):
        return self.__code

    def getMessage(self):
        return self.__message

    def __str__(self):
        return ("%d - %s" % (self.getCode(), self.getMessage().encode('utf-8')))


class HttpException(BaseSdkException):

    def __init__(self, code, message):
        BaseSdkException.__init__(self, code, message)


class ServiceException(BaseSdkException):

    def __init__(self, code, message):
        BaseSdkException.__init__(self, code, message)
