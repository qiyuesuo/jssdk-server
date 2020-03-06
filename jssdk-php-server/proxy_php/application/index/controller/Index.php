<?php
namespace app\index\controller;

use app\HttpClient;
use app\HttpParameter;

header('Access-Control-Allow-Origin:*');
header('Access-Control-Allow-Methods:GET, POST, OPTIONS, PATCH, PUT, DELETE');
header('Access-Control-Allow-Headers:XMLHttpRequest, X_FILENAME, accept-encoding, authorization, content-type, dnt, origin, user-agent, x-csrftoken, x-requested-with, nomes');
header('Access-Control-Allow-Credentials:false');

class Index {
    private $accessToken = '';
    private $accessSecret = '';
    private $serverUrl = '';

    public function index(){
        $httpClient = new HttpClient($this->accessToken, $this->accessSecret, $this->serverUrl);
        $url = $this->serverUrl.'/uniformapi';

        $request_all = array_merge($_POST, $_GET);
        $requestData = isset($request_all['requestData']) && $request_all['requestData'] ? $request_all['requestData'] : '';
        // get请求
        if($_SERVER['REQUEST_METHOD'] == 'GET'){
            $paramers = HttpParameter::httpGetParamer();
            $paramers->addParam('requestData', $requestData);
            $result = $httpClient->doDownload($url, $paramers);
            echo $result;
        }
        // post请求
        if($_SERVER['REQUEST_METHOD'] == 'POST'){
            $file = isset($_FILES['file']) ? $_FILES['file'] : null;//	上传的文件
            if(!is_null($file)){
                $file = curl_file_create($_FILES['file']['tmp_name']);
            }
            $paramers = HttpParameter::httpPostParamer();
            $paramers->addParam('requestData', $requestData);
            $paramers->addParam('file', $file);
            $response = $httpClient->doPost($url, $paramers);
            echo $response;
        }
    }
}
