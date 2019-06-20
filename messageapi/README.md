# handy-message-api

> get message data 

## api
##### 1 get history message list
```
method:post
url:http://127.0.0.1:9090/plugins/handy-message-api/api
paramter:
{
    "userName":"user1",
    "pageIndex":1,
    "pageSize":10,
    "messageType":"HISTORY"
}
```
##### 2 get chat message list
```
method:post
url:http://127.0.0.1:9090/plugins/handy-message-api/api
paramter:
{
    "userName":"user1",
    "pageIndex":1,
    "pageSize":10,
    "messageType":"LIST"
}
```

