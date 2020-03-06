<?php
namespace app;
use Exception;

class HttpClient {

    const RENNECT_TIMES = 2;            // 请求失败后重试次数
    const SSL_CHECK = false;            // 忽略SSL检查
    const CONNECT_TIMEOUT = 15;
    const READ_TIMEOUT = 30;

    private $accessKey;                 // appToken
    private $accessSecret;              // appSecret
    private $serverUrl;                 // 契约锁服务地址

    public function __construct($accessKey, $accessSecret, $serverUrl) {
        $this->accessKey = $accessKey;
        $this->accessSecret = $accessSecret;
        $this->serverUrl = $serverUrl;
    }

    /**
     * 获取当前时间戳
     */
    private function getMillistime(){
        $microtime = microtime();
        $comps = explode(' ', $microtime);
        return sprintf('%d%03d', $comps[1], $comps[0] * 1000);
    }

    /**
     * 获取http请求头
     */
    public function getHeaders(){
        $time = $this->getMillistime();
        $signature = md5(str_replace(' ', '',$this->accessKey.$this->accessSecret.$time));

        $headers = array(
            'x-qys-open-accesstoken:'.$this->accessKey,
            'x-qys-open-signature:'.$signature,
            'x-qys-open-timestamp:'.$time
        );
        return $headers;
    }

    /**
     *
     * 构建http请求参数
     *
     */
    public function buildHttpRequest($curl, $url, $heads){
        curl_setopt($curl, CURLOPT_TIMEOUT, self::READ_TIMEOUT);
        curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, self::CONNECT_TIMEOUT);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, self::SSL_CHECK);
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, self::SSL_CHECK);
        //设定是否显示头信息
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $heads);
        return $curl;
    }

    function doService($url,HttpParameter $httpParameters){
        switch ($httpParameters->getHttpMethod()) {
            case HttpMethod::GET:
                $result = $this->doGet($url, $httpParameters->getParams());
                break;
            case HttpMethod::POST:
                $result = $this->doPost($url, $httpParameters->getParams());
                break;
            default:
                $result = null;
                break;
        }
        return $result;
    }

    function doServiceWithJson($url, $json, $headers){
        $flag=1;
        while($flag <= self::RENNECT_TIMES) {
            try {
                if (!function_exists('curl_init')) {
                    throw new Exception("CURL扩展没有开启!");
                }
                $curl = curl_init();
                array_push($headers, 'Content-Type:application/json;charset=UTF-8');
                $curl = $this->buildHttpRequest($curl, $url, $headers);
                // 提交post请求
                curl_setopt($curl, CURLOPT_POSTFIELDS, $json);
                $output = curl_exec($curl);
                if ($output === false) {
                    $res = array(
                        "code" => 1001,
                        "message" => curl_error($curl)
                    );
                    $output = json_encode($res);
                }
                // 关闭CRUL
                curl_close($curl);
            } catch (Exception $exc) {
                // 关闭CRUL
                curl_close($curl);
                $res = array(
                    "code" => 1001,
                    "message" => $exc->getMessage()
                );
                $output = json_encode($res);
            }
            if($output){
                break;
            }
            $flag++;
        }
        return $output;
    }

    function doDownload($url, HttpParameter $httpParameters){
        $result = $this->doGet($url,$httpParameters);
        //判断是否返回文件流
        $array_output = json_decode($result, true);
        if(is_array($array_output) && array_key_exists("code",$array_output) && $array_output['code']!==0){
            return array(
                "code" => $array_output['code'],
                "message" => $array_output['message']
            );
        }
        return $result;
    }

    /**
     * GET请求
     * @param $url
     * @param $heads
     * @param $data
     * @return mixed|string
     */
    function doGet($url,HttpParameter $data){
        $flag=1;
        while($flag <= self::RENNECT_TIMES){
            try {
                // 请求参数有值时构建get请求
                $urlParams = $data->getQueryString();
                if ($urlParams) {
                    $url = $url.'?'.$urlParams;
                }
                if (!function_exists('curl_init')) {
                    throw new Exception("CURL扩展没有开启!");
                }
                $curl = curl_init();
                $curl = $this->buildHttpRequest($curl, $url, $this->getHeaders());
                $output = curl_exec($curl);
                if ($output === false) {
                    $res = array(
                        "code" => 1001,
                        "message" => curl_error($curl)
                    );
                    $output = json_encode($res);
                }
                curl_close($curl);
            } catch (Exception $exc) {
                curl_close($curl);
                $res = array(
                    "code" => 1001,
                    "message" => $exc->getMessage()
                );
                $output = json_encode($res);
            }
            if($output){
                break;
            }
            $flag++;
        }
        return $output;
    }

    /**
     * POST请求
     * @param $url
     * @param $heads
     * @param $data
     * @return mixed|string
     */
    function doPost($url, HttpParameter $data){
        $flag=1;
        while($flag <= self::RENNECT_TIMES) {
            try {
                if (!function_exists('curl_init')) {
                    throw new Exception("CURL扩展没有开启!");
                }
                $curl = curl_init();
                $curl = $this->buildHttpRequest($curl, $url, $this->getHeaders());
                //启用时会发送一个常规的POST请求，类型为：application/x-www-form-urlencoded，就像表单提交的一样。
                curl_setopt($curl, CURLOPT_POST, 1);
                // 提交post请求
                curl_setopt($curl, CURLOPT_POSTFIELDS, $data->getParams());
                $output = curl_exec($curl);
                if ($output === false) {
                    $res = array(
                        "code" => 1001,
                        "message" => curl_error($curl)
                    );
                    $output = json_encode($res);
                }
                // 关闭CRUL
                curl_close($curl);
                return $output;
            } catch (Exception $exc) {
                // 关闭CRUL
                curl_close($curl);
                $res = array(
                    "code" => 1001,
                    "message" => $exc->getMessage()
                );
                $output = json_encode($res);
            }
            if($output){
                break;
            }
            $flag++;
        }
        return $output;
    }

}