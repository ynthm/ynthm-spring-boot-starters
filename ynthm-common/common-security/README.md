# Security

## bouncycastle

在 RSA 加密中，PSS（Probabilistic Signature Scheme）是一种用于数字签名的方案，提供更高的安全性。PSS 参数通常包括以下几个方面：

PSS 参数
- Salt长度： 指定在生成签名时用于哈希的盐值长度。
- Hash算法： 用于生成签名的哈希函数，常用的有 SHA-256、SHA-512 等。
- Mask生成算法： 用于生成掩码的算法，通常是通过哈希函数实现的。