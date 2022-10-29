# MinIO

## 启动服务

```sh
minio server ~/Apps/miniodata/ --console-address :9090
```

可以用用户名与密码，也可以使用 Identity > Service Accounts > Create service account

## presigned URL

**用户没有 S3 bucket的权限，但是想要访问bucket里面的文件**，（这里的访问，有多种方式，比如说下载，上传等），那么在这种情况下，可以使用presigned URL来解决这个问题。

presigned URL顾名思义，就是加了签名的URL，这里的签名相当于是给这个URL一个临时的权限，

## 附录

- https://min.io/docs/minio/linux/developers/java/API.html