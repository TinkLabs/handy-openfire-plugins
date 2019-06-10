# docker openfire
## 安装

##### 拉取镜像
```
# 查找镜像
docker search openfire
# 拉取镜像
docker pull sameersbn/openfire
```

##### 上传镜像
```
# 登录aws
aws ecr get-login --no-include-email --region ap-southeast-1
# 打标签
docker tag sameersbn/openfire:latest 204328232493.dkr.ecr.ap-southeast-1.amazonaws.com/handy-internal-openfire-dev:latest
# 上传
docker push 204328232493.dkr.ecr.ap-southeast-1.amazonaws.com/handy-internal-openfire-dev:latest
```