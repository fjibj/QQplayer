# QQplayer
基于SmartQQ的一个定时抓取和发送每日新闻的QQ机器人


启动脚本：
#进入smartqq-master.jar所在目录
cd /d E:\ML\smartqq-master\out\artifacts
#设置环境变量JAVA_HOME，并%JAVA_HOME%\bin加入PATH
set JAVA_HOME=E:\Java\jdk1.7.0_80
set PATH=%JAVA_HOME%\bin;%PATH%
#启动smartqq-master.jar定时抓取每日新闻（现代消费导报）并发送到指定QQ群
#java -cp smartqq-master.jar WebContent "群名称" hour minute second 多长时间执行一次（小时） 当天还是第二天（1：第二天，0：当天）
java -cp smartqq-master.jar WebContent "Just for test" 9 0 0 24 1 #表示于第二天早上9点开始执行，之后每隔24小时（一天）执行一次
pause
