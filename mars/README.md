# 开发软件要求

除了Java和Eclipse之外，需要安装Maven和m2e(maven的eclipse插件)。软件以及安装步骤到官网
上查看，选择最新版本即可。
Maven Site: http://maven.apache.org/download.cgi
m2e Site: https://www.eclipse.org/m2e/download/


# 配置步骤

只有Maven的私服地址需要配置，把fps根目录下的setting.xml文件copy到{user.home}/.m2文件夹下即可。
暂时Maven私服放在227。


## Eclipse开发环境搭建

1. 切换到fps目录，运行`mvn install`指令，会需要一段时间，耐心等待。第一次build，maven会下载相关
   的jar包和plugin到本地。
2. Eclipse环境下，右键 --> Import --> Maven --> Existing Maven Projects, 选择fps目录，
   finish即可。

## Database配置

开发Database需要经210.22.151.39跳转到192.168.8.116，端口依然是1521，用户名，密码都是fps，需要用到
putty等工具跳转。需要配置DB的地方有两个，application.properties和generatorConfig.xml。

## MyBatis的entity自动生成

该步骤放在这里可能不是很合适，**需要新生成Mybatis文件的时候才会用到**。

1. 切换到fps-server, 编辑src/main/resources/generatorConfig.xml，一般只需要启用需要生成的table，
   其它的请注释掉。
2. 切换回fps-server，运行`mvn mybatis-generator:generate`

