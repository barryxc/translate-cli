## 百度翻译 cli 工具 

* [项目地址](https://github.com/barryxc/translate-cli)

* 工程目录下新增 app.properties 配置文件，添加百度应用ID和秘钥

```text
APP_ID=你的APPID
SECURITY_KEY=你的秘钥
```

* 安装 cli 工具

```shell

./gradlew installDist
```

* 翻译文本

```shell
translate-cli '文本内容'
```

