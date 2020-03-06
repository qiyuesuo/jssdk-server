<?php
namespace app;
class HttpParameter {

    private $params;        // 参数
    private $httpMethod;    // 请求方式
    private $jsonParams;    // json参数

    /**
     * HttpParameter constructor.
     * @param $httpMethod
     */
    public function __construct($httpMethod) {
        $this->httpMethod = $httpMethod;
    }

    /**
     * post请求
     * @return HttpParameter
     */
    public static function httpPostParamer(){
        return new HttpParameter(HttpMethod::POST);
    }

    /**
     * get请求
     * @return HttpParameter
     */
    public static function httpGetParamer(){
        return new HttpParameter(HttpMethod::GET);
    }

    /**
     * 获取url参数（一般用于get请求）
     * @return null|string
     */
    public function getQueryString(){
        $urlParams = $this->getParams();
        if(is_null($urlParams)){
            return null;
        }
        if($urlParams && is_array($urlParams)){
            $paramers = http_build_query($urlParams);
            return $paramers;
        }
        return null;
    }

    /**
     * @return mixed
     */
    public function getParams()
    {
        return $this->params;
    }

    public function addParam($name, $value){
        $params = $this->params;
        $params[$name] = $value;
        $this->params = $params;
    }

    /**
     * @return mixed
     */
    public function getHttpMethod()
    {
        return $this->httpMethod;
    }

    /**
     * @return mixed
     */
    public function getJsonParams()
    {
        return $this->jsonParams;
    }

    /**
     * @param mixed $jsonParams
     */
    public function setJsonParams($jsonParams)
    {
        $this->jsonParams = $jsonParams;
    }

    /**
     * 是否是json请求
     */
    public function isJson(){
        if(is_null($this->jsonParams)){
            return false;
        }
        return true;
    }
}