# MinIO

## 启动服务

```sh
minio server ~/Apps/miniodata/ --console-address :9090
```

可以用用户名与密码，也可以使用 Identity > Service Accounts > Create service account

## 上传对象 putObject

```java
public ObjectWriteResponse putObject(PutObjectArgs args)
```

- 添加的Object大小不能超过5 GB。
- 默认情况下，如果已存在同名Object且对该Object有访问权限，则新添加的Object将覆盖原有的Object，并返回200 OK。
- OSS没有文件夹的概念，所有资源都是以文件来存储，但您可以通过创建一个以正斜线（/）结尾，大小为0的Object来创建模拟文件夹。

## getPresignedObjectUrl

获取一个指定了 HTTP 方法、到期时间和自定义请求参数的对象URL地址，也就是返回带签名的URL，这个地址可以提供给没有登录的第三方共享访问或者上传对象。

```java
public String getPresignedObjectUrl(GetPresignedObjectUrlArgs args) 
```

## getPresignedPostFormData

使用此方法，获取对象的上传策略（包含签名、文件信息、路径等），然后使用这些信息采用POST 方法的表单数据上传数据。也就是可以生成一个临时上传的信息对象，第三方可以使用这些信息，就可以上传文件。

一般可用于，前端请求一个上传策略，后端返回给前端，前端使用Post请求+访问策略去上传文件，这可以用于JS+SDK的混合方式集成

```java
public Map<String,String> getPresignedPostFormData(PostPolicy policy)
```

## presigned URL

**用户没有 S3 bucket的权限，但是想要访问bucket里面的文件**，（这里的访问，有多种方式，比如说下载，上传等），那么在这种情况下，可以使用presigned URL来解决这个问题。

presigned URL顾名思义，就是加了签名的URL，这里的签名相当于是给这个URL一个临时的权限，

## 分片上传

```java
public CompletableFuture<ObjectWriteResponse> composeObject(ComposeObjectArgs args) 
```

1. 前端服务进行大文件分片处理，将分片信息传递给文件服务，文件服务返回所有分片的上传链接及uploadId。
1. 前端服务直接请求Minio 服务器，并发上传分片
1. 所有分片上传完成后，使用uploadId 调用文件服务进行文件合并

## 秒传

计算文件 hash，并持久化 bucketName objectName hash。

## 分片下载

- statObject 查询对象大小，根据大小计算分片
- getObject offset length 分段获取流

## 附录

- https://min.io/docs/minio/linux/developers/java/API.html