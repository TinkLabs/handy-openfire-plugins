# docker openfire
## 安装
镜像构建基于: https://github.com/sameersbn/docker-openfire
##### 创建镜像
```
# 进入Dockerfile 文件所在目录
cd docker 
# 构建镜像
docker build -t xiaoyu/openfire .
```

##### 上传镜像
```
# 登录aws
aws ecr get-login --no-include-email --region ap-southeast-1
# 打标签
docker tag xiaoyu/openfire:latest 204328232493.dkr.ecr.ap-southeast-1.amazonaws.com/handy-internal-openfire-dev:latest
# 上传
docker push 204328232493.dkr.ecr.ap-southeast-1.amazonaws.com/handy-internal-openfire-dev:latest
```
