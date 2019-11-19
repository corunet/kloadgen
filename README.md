# KLoadGen - Kafka + Avro Load Generator

[![Build Status](https://travis-ci.org/corunet/kloadgen.svg?branch=master)](https://travis-ci.org/corunet/kloadgen) [![Coverage Status](https://coveralls.io/repos/github/corunet/kloadgen/badge.svg?branch=master&maxAge=0)](https://coveralls.io/github/corunet/kloadgen?branch=master)

___

KLoadGen is kafka load generator plugin for jmeter designed to work with AVRO schema Registries. It allows to send kafka messages with a structure defined as an AVRO Subject. It connects to the Scheme Registry Server, retrieve the subject to send and generate a random messages every time.
## Getting Started
___

KLoadGen includes four main components

* **KLoadGen Kafka Sampler** : This is jmeter java sampler sends messages to kafka.
* **KLoadGen Config** : This jmeter config element generates plaintext messages based on input schema template designed.
* **Kafka Headers Config** : This jmeter config element generates serialized object messages based on input class and its property configurations.

### Setup
___

#### Requirement

KLoadGen uses Java, hence on JMeter machine JRE 8 or superior:

Install openjdk on Debian, Ubuntu, etc.,
```
sudo apt-get install openjdk-8-jdk
``` 

Install openjdk on Fedora, Oracle Linux, Red Hat Enterprise Linux, etc.,
```
su -c "yum install java-1.8.0-openjdk-devel"
```
For windows and mac and you can:
 * download oracle JDK 8 setup from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
 * using chocolatey (windows):
        https://chocolatey.org/packages?q=java
   brew (mac):
```
    brew tap adoptopenjdk/openjdk 
    brew cask install adoptopenjdk8 
```


#### Build Project
```
mvn clean install
```
Once build is completed, copy jar file to JMETER_HOME/lib/ext directory.

### KLoadGenSampler
___

* **bootstrap.servers** : broker-ip-1:port, broker-ip-2:port, broker-ip-3:port
* **zookeeper.servers** : zookeeper-ip-1:port, zookeeper-ip-2:port, zookeeper-ip-3:port. **Note** : Any one of bootstrap or zookeeper server detail is enough. if zookeeper servers are given then bootstrap.servers are retrieved dynamically from zookeeper servers.
* **kafka.topic.name** : Topic on which messages will be sent
* **key.serializer** : Key serializer (This is optional and can be kept as it is as we are not sending keyed messages).
* **value.serializer** : For plaintext config element value can be kept same as default but for serialized config element, value serializer can be "ObjectSerializer"
* **compression.type** : kafka producer compression type(none/gzip/snappy/lz4)
* **batch.size** : messages batch size(increased batch size with compression like lz4 gives better throughput)
* **linger.ms** : How much maximum time producer should wait till batch becomes full(should be 5-10 when increased batch size and compression is enabled)
* **buffer.memory** : Total buffer memory for producer.
* **acks** : Message sent acknowledgement, value can be (0/1/-1).
* **send.buffer.bytes** : The size of the TCP send buffer (SO_SNDBUF) to use when sending data. If the value is -1, the OS default will be used.
* **receive.buffer.bytes** : The size of the TCP receive buffer (SO_RCVBUF) to use when reading data. If the value is -1, the OS default will be used.
* **security.protocol** : kafka producer protocol. Valid values are: PLAINTEXT, SSL, SASL_PLAINTEXT, SASL_SSL.
* **message.placeholder.key** : Config element message variable name. This name should be same as message placeholder key in serialized/plaintext config element.
* **kerberos.auth.enabled** : YES/NO if it is disabled all below properties will be ignored
* **java.security.auth.login.config** : jaas.conf of kafka Kerberos
* **java.security.krb5.conf** : Kerberos server krb5.conf file
* **sasl.kerberos.service.name** : Kafka Kerberos service name

Above properties are added by default in sampler as those are more significant in terms of performance in most of the cases. But you can add other non listed kafka properties with prefix "_".

For example to enable SSL properties you can add below properties

```
_ssl.key.password
_ssl.keystore.location
_ssl.keystore.password
_ssl.truststore.location
_ssl.truststore.password

```

### Schema Template Functions
___

KLoadGen provides an easy way for random data generation base on the field type.

| Type | Details |  Returns |
|----------|:-------:|:--------:|
| string | Field of String type| Random string with a longitude of 20 characters |
| int | Field of Int type | Random Integer |
| short | Field of short type | Random Short |
| long | Field of long type | Random Long |
| enum | Field of enum type | Random enum value bases on the AVRO enum type definition |
| union | Field of type UNION | Random long* (in the future we will generate base on the union types) |
| stringTimestmap | Field of type String but coding a Timestmap | Localdatetime.now formatted as string |
| longTimestmap | Field of type Long but coding a Timestmap | Localdatetime.now formatted as long |

Other values will be considered Constants for this field and will be converted to the Field Type. Keep that in mind to avoid Cast Exceptions

## Special Thanks!

* We would like to special thanks to [pepper-box
](https://github.com/GSLabDev/pepper-box) for give us the base to create this plugin and the main ideas about how to face it.