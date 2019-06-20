# Install Openfire in AWS EC2

### 安装JDK
```
# 下载
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u141-b15/336fa29ff2bb4ef291e347e091f7f4a7/jdk-8u141-linux-x64.tar.gz"
# 解压
tar -zxvf jdk-8u141-linux-x64.tar.gz
# 移动到新的文件夹中
cd  /usr/local/src
sudo mkdir jdk
cd ~
sudo mv jdk1.8.0_141 /usr/local/src/jdk
# 添加环境变量
sudo vim /etc/profile

export JAVA_HOME=/usr/local/src/jdk/jdk1.8.0_141
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
# 立即生效
source /etc/profile
# 验证jdk 
java -version
```

### 安装openfire
```
# 下载
wget -Oopenfire_4_3_2.tar.gz https://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_4_3_2.tar.gz
# 解压
tar -xzvf openfire_4_3_2.tar.gz
# 移动到opt 
sudo mv openfire /opt
# 启动
cd /opt/openfire/bin
./openfire {start|stop}
```
