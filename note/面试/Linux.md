### 1. 如何查看占用端口80的进程？（如何查看占用xxxx端口的进程）

`netstat -tunlp | grep 80`：查看端口占用

`lsof -i:80`

### 2. 让一个命令（脚本）每周五一个小时执行一次如何做？

分析：操作系统层面的计划任务/定时任务

-   Linux系统
-   Windows系统

方法：命令，脚本

-   Crond服务：定时任务
    -   `crontab -l`：查看定时任务
    -   `crontab -e`：编辑定时任务

### 3. 如何在Accesslog中找出访问量最高的IP地址？

分析：

1.   Nginx
2.   日志内容格式
3.   Shell命令高级用法
4.   AWK命令
5.   sort命令
6.   sort命令
7.   /var/log/nginx/access.log 
     1.   格式

### 4. TOP命令

### 5. 如何查看应用占用内存情况

1.   如何区分应用或服务
     1.   端口
     2.   端口保活
2.   找到某端口号对应的服务
     1.   `ps -aux | grep 80`
3.   top -p

### 6. 防火墙

-   C6：

    `Iptables`

-   C7：

    `firewalld`

	### 7. 如何监控Docker

1.   没有容器云管理平台
     1.   Docker自带命令
          1.   docker container top id
          2.   docker container status
     2.   Zabbix（图形） + Grafnan
     3.   Prometheus + Grafnan
2.   有容器管理平台