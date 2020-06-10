# Getting Started

### 简介

该服务用于与契约锁JS-SDK搭配使用，实现了后端转发逻辑，对接方需要针对自身情况以该项目的实现作为参考进行开发，文档参考地址：[契约锁开放平台文档](https://open.qiyuesuo.com/document/2593807238267146934)

### 指南
对接者需要按照下方指示来使用该示例:

* 打开项目中的`Index`类，修改其中的`HttpClient`的构造方法为对接方自身的配置，需要指定对接方在契约锁开放平台申请的`access token`、`access secret`和转发的契约锁接口地址，测试环境接口地址： [https://openapi.qiyuesuo.cn](https://openapi.qiyuesuo.cn)，正式环境接口地址：[https://openapi.qiyuesuo.com](https://openapi.qiyuesuo.com)；
* 该项目需要配合契约锁`JS-SDK`使用，在项目启动后，将项目接口地址写入`JS-SDK`的构造函数中，初始化`JS-SDK`；
* 该项目是基于`PHP 5.4` 和 `ThinkPHP_5.0.24` 实现的，若对接方与此版本不一致，需要对示例中的代码做相应的兼容调整，以保证其正常运行，若不使用`ThinkPHP_5.0.24`,可将目录jssdk-php-server\proxy_php\application\index\controller下的index.php文件及jssdk-php-server\proxy_php\application下的HttpClient.php、HttpMethod.php、HttpParameter.php文件单独提取出来使用，做相应的兼容调整；
* 项目启动后，`JS-SDK`填写的默认转发地址应该为`[host]/proxy_php/public/index.php`,若需要调整请自行调整。

### 关于

契约锁 成立于2015年，是新一代数字签名服务领域的领军企业。依托雄厚的企业管理软件服务经验，致力为全国的企业及个人用户提供最具可用性、稳定性及前瞻性的电子签署、数据存证及数据交互服务。 契约锁深耕企业级市场，产品线涵盖电子签署、合同管理、公文签发、数据存证等企业内签署场景，并提供本地签、远程签、标准签等多种API调用方式接入企业内部管理系统。目前主要为教育、旅游、互联网金融、政府事业单位、集团企业、B2B电商、地产中介、O2O等企业及个人提供签署、存证服务。 契约锁严格遵守《中华人民共和国电子签名法》，并联合公安部公民网络身份识别系统（eID）、工商相关身份识别系统、权威CA机构、公证处及律师事务所，确保在契约锁上签署的每一份合同真实且具有法律效力。 契约锁平台由上海亘岩网络科技有限公司运营开发，核心团队具有丰富的企业管理软件、金融支付行业、数字证书行业从业经验，致力于通过技术手段让企业合同签署及管理业务更简单、更便捷。

了解更多契约锁详情请访问 www.qiyuesuo.com.