# 巴法云远程控制PC关机
使用巴法云发送指令远程控制PC

可扩展其他功能

## 使用方式
编译或下载Release中已打包好的Jar文件

### 命令模板

```
javaw -jar <jarfile> <uid> <topic> <command>
```

#### 参数解析
> `jarfile`: Jar文件路径
> 
> `uid`: 巴法云私钥
> 
> `topic`: 订阅主题
> 
> `command`: `shutdown`命令参数

### 示例
```
javaw -jar bamfa-shutdown.jar xxxxxxxxxxxx Computer001 -s -t 10
```

## 内置指令
> off

关闭电脑

> exit

退出程序
