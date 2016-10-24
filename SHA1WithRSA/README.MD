# 基于 *SHA1WithRSA* 的签名与验签

## 简介

1. *SHA1安全哈希算法* `Secure Hash Algorithm` 主要适用于数字签名标准 `Digital Signature Standard DSS` 里面定义的 *数字签名算法* `Digital Signature Algorithm DSA` .
2. *RSA* 是目前最有影响力的公钥加密算法, 它能够抵抗到目前为止已知的绝大多数密码攻击, 已被ISO推荐为公钥数据加密标准.
3. *SHA1WithRSA* 表示: 用 *SHA1* 算法进行签名, 用 *RSA* 算法进行加密.

## *RSA* 算法的 *私钥* 、 *公钥* 与 *pkcs#8私钥* 的生成方法

1. 生成私钥

```bash
openssl genrsa -out private_key.pem 1024
```

2. 生成公钥

```bash
openssl rsa -in private_key.pem -pubout -out public_key.pem
```

3. 将pem格式私钥转为pkcs#8格式

```bash
openssl pkcs8 -topk8 -inform PEM -in private_key.pem -outform PEM -nocrypt -out private_key_pkcs8.pem
```

## 参考文献

[1] 王靖伟. 基于多点随机触控的异步高并发签到系统的设计与实现[D]. 天津商业大学, 2016. 37-38. [[pdf](http://jingweiwang.github.io/pdf/基于多点随机触控的异步高并发签到系统的设计与实现_王靖伟.pdf)]