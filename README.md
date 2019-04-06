# Library of Alexandria

Library of Alexandria (LoA in short) is a project that aims to collect and archive PDF documents from the internet.

In our modern age new text documents are born in a blink of an eye then (often just as quickly) disappear from the internet. We find it a noble task to save these documents for future generations.

This project aims to support this noble goal in a scalable way. We want to make the archival activity streamlined and easy to do even in a huge (Terabyte / Petabyte) scale. This way we hope that more and more people can start their own collection helping the archiving effort.

## Prerequisites

We tried to keep the prerequisites of the project on the minimum because one of the goals of the project is to make it easy to start archiving even with a limited technical knowledge.

The list of required software to start archiving:
- [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) or greater
- [MySQL 8.15](https://dev.mysql.com/downloads/mysql/8.html) or greater

The list of required software to be able to search documents:
- [Elasticsearch 6.6](https://www.elastic.co/downloads/past-releases/elasticsearch-6-6-2)

## System Requirements

It's important to mention the hardware requirements of collecting and handling documents. If you only want to gather a few million of documents then that could be done on a home PC. However, if you want to collect & store a massive amounts then purpose built hardware is necessary. This hardware is not necessarily expensive however. Also if you want to collect 24/7 then having a machine that doesn't consume a lot of wattage is recommended where electricity prices are high.

Collecting and indexing have quite a different requirements as well.

The requirements mentioned here could be built from used commodity hardware if necessary.

### Collecting

Collecting is easier and cheaper than indexing the documents but doesn't provide any search capability.

Memory requirements for collecting varies on your failure rate. If you have a lots of URLs that fail to download then storing those failures in the SQL database will require quite a lot of RAM. However if you have small failure rate then storing even 5-10 million of rows in SQL could be done from 8-10 GB of ram.

#### Collecting small amounts (1 - 5 million)

* **CPU**: [Intel Pentium G4400](https://ark.intel.com/content/www/us/en/ark/products/88179/intel-pentium-processor-g4400-3m-cache-3-30-ghz.html) or similar
* **Memory**: 16-24 GB DDR3/DDR4 (depending on download failure rate)
* **Disc space**: ~20 TB

#### Collecting medium amounts (5 - 25 million)

WIP...

#### Collecting massive amounts (25-100+ million)

WIP...

### Indexing

Indexing is much more CPU and memory intensive than collecting but requires a lot less disk space. Indexing should either be run on a different machine than collecting or the memory and disc requirements should be added together.

You can do the indexing on any machine that have the sufficient memory requirements, but without a proper CPU your indexing speed could be very slow.

#### Indexing small amounts (1 - 5 million)

* **CPU**: [Intel Core i5-7700](https://ark.intel.com/content/www/us/en/ark/products/97128/intel-core-i7-7700-processor-8m-cache-up-to-4-20-ghz.html) or similar
* **Memory**: 24 GB
* **Disc space**: 250 GB

#### Indexing medium amounts (5 - 25 million)

WIP...

#### Indexing massive amounts (25-100+ million)

WIP...

## Installation

Installing the applications and the prerequisite software is quite straightforward. At this time we provide a guide to the Windows based systems. Installing LoA on Linux systems are supported as well but requires more technical knowledge. An ideal deployment is running the apps in separate VMs or Docker containers but for the sake of simplicity we are not doing that in this guide. In the future we will create a more advanced guide.

### Installing Java

First you need to download the Java 11 Runtime Environment. It's available [here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). After the download is complete you should run the installer and follow the directions it provides until the installation is complete.

Once it's done, if you open a command line (write cmd to the Start menu's search bar) you will be able to use the java command. Try to write `java -version`. You should get something similar:

```
java version "11" 2018-09-25
Java(TM) SE Runtime Environment 18.9 (build 11+28)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11+28, mixed mode)
```

### Installing MySQL

Download MySQL 8.15 from [here](https://dev.mysql.com/downloads/mysql/8.html). After the download is complete run the installer and follow the directions it provides. If it's possible install the MySQL Workbench tool as well because you will need it later for administrative tasks.

### Running the database application

You can download the database application files at our [release](https://github.com/bottomless-archive-project/library-of-alexandria/releases) page. Please take care to choose a non "pre-release" version!

After the download is complete run the application via the following command:

```
java -jar loa-database-application-{release-number}.jar ...
```

In the place of the ... you should write the various parameters. For the available parameters check the parameter list under the downloader application.

For example:

```
java -jar loa-database-application-1.0.0-milestone.1.jar --spring.datasource.url=jdbc:mysql://localhost/loa?useSSL=false&useUnicode=yes&characterEncoding=utf8&serverTimezone=UTC --spring.datasource.username=root --spring.datasource.password=root
```

Running the database application is necessary after every release to update the database schema. However, once it's finished running it will shut down and not needed to be run again until a new LoA update.

### Running the crawler application

You can download the crawler application files at our [release](https://github.com/bottomless-archive-project/library-of-alexandria/releases) page. Please take care to choose a non "pre-release" version!

After the download is complete run the application via the following command:

```
java -jar loa-downloader-application-{release-number}.jar ...
```

In the place of the ... you should write the various parameters. For the available parameters check the parameter list under the downloader application.

For example:

```
java -jar loa-downloader-application-1.0.0-milestone.1.jar --loa.source.name=test --loa.source.file.location=C:\loa\source.txt --loa.vault.location.file.path=C:\loa\vault\ --loa.stage.location=C:\loa\stage\ --spring.datasource.url=jdbc:mysql://localhost/loa?useSSL=false&useUnicode=yes&characterEncoding=utf8&serverTimezone=UTC --spring.datasource.username=root --spring.datasource.password=root
```

### Installing Elasticsearch

WIP!!!

## Applications

The Library of Alexandria project consist of more than one (usually) scalable applications. Not all of them are mandatory for the archiving effort. Some of them are created for administrating or maintenance purposes.

### Database Application

The LoA project stores it's crawl related data in a MySQL database. Occasionally between releases a database migration is necessary. This application's goal is to streamline this process. Also in the future it will provide basic database administrator tasks (query statistics, initiate the re-crawling of failed tasks etc).

### Downloader Application

This application is responsible for downloading the document files.

#### Parameters

| Parameter                                | Description   |
| ---------------------------------------- |:------------- |
| **loa.downloader.version-number**        | This version number will be saved to the database for every document that has been crawled. Later on, if it will be necessary to run cleanup or fixing database tasks that are specific to a given version will be checked by the value of this version number. This way it will be easier to fix bugs or database inconsistencies introduced by a specific crawler version. Please do not change this otherwise the official migration/fixer utilities are not going to be usable. |
| **loa.downloader.executor.thread-count** | How many download should run simultaneously. Usually this number should be set according to your network or storage device (HDD/SSD) speed. If you want to tune the collecting speed of documents then increase this as long as one of them is fully saturated. Be careful however, if you have a subpar router or networking infrastructure then many simultaneous requests could cause timeouts, overheating on routers and thus making the tuning of this parameter counter intuitive. *(Default value: 100)* |
| **loa.downloader.executor.queue-length** | How many locations do we want to pre-calculate. This queue are fed by the source subsystem with URL locations to crawl and is being consumed by the downloader subsystem. If you set this parameter too high it could cause out of memory errors while if it is set to too low then most of the download threads could idle. The suggested value is between 1000 - 50000 depending on the available memory. *(Default value: 1000)* |
| **loa.source.name**                      | The name of the source location. This name will be saved to the database for every crawled document. It could be helpful for statistics collection or in identifying bugs and errors with specific sources. *(Default value: unknown)* |
| **loa.source.type**                      | Describes the type of the source. Could be either `file` or `commoncrawl`. If the value is file then the location data will be loaded from a local file. If it is commoncrawl then the parser will start loading and parsing urls from the [Common Crawl](http://commoncrawl.org/) corpus. *(Default value: file)* |
| **loa.source.commoncrawl.crawl-id**      | Used only when `loa.source.type` is set to `commoncrawl`. The id of the Common Crawl crawling sentence. For example the [2019 January](http://commoncrawl.org/2019/01/january-2019-crawl-archive-now-available/)'s id is CC-MAIN-2019-04. You can acquire the crawl id for each month's corpus [here](http://commoncrawl.org/the-data/get-started/). |
| **loa.source.commoncrawl.warc-id**       | Used only when `loa.source.type` is set to `commoncrawl`. Every month's Common Crawl crawling sentence is built from multiple [WARC](https://en.wikipedia.org/wiki/Web_ARChive) files (mostly around 64000). This is the id of the WARC file that the parsing should start from. When you first start crawling a month's crawl sequence this should be 1. When the downloader opens a new WARC file it will print it's id to the console. If you need to stop the crawler and want to re-start it from where it stopped then write down the last crawled WARC id and set this parameter to it. |
| **loa.source.file.location**             | Used only when `loa.source.type` is set to `file`. The location of the source file on the disk. It's not a problem if it contains non-pdf files. |
| **loa.source.file.encoding**             | Used only when `loa.source.type` is set to `file`. It can be set to `none` or gzip. If it's set to `none` then the file will be read as a non-compressed file. If it's set to `gzip` then it will be red as a gzipped file, being unzipped on the fly. *(Default value: none)* |
| **loa.vault.location.type**              | Describes the type of the vault's location. Could be either `file` or `smb`. *(Default value: file)* |
| **loa.vault.location.file.path**         | WIP           |
| **loa.vault.location.smb.host**          | WIP           |
| **loa.vault.location.smb.share-name**    | WIP           |
| **loa.vault.location.smb.share-path**    | WIP           |
| **loa.vault.location.smb.username**      | WIP           |
| **loa.vault.location.smb.password**      | WIP           |
| **loa.vault.location.smb.domain**        | WIP           |
| **loa.stage.location**                   | WIP           |
| **loa.checksum.type**                    | WIP           |
| **spring.datasource.driver-class-name**  | WIP           |
| **spring.datasource.url**                | WIP           |
| **spring.datasource.username**           | WIP           |
| **spring.datasource.password**           | WIP           |

#### Profiles

WIP

## Domain language

WIP!!!

1. **Vault**: The location where the collected documents are saved.
2. **Document**: A document collected from the internet.
3. **Document content**: ...
3. **Staging area**: A temporary location where the collected documents placed for post processing before going to the archive.
4. **Source**: ...
5. **Failure rate**: How many documents fail to download compared to the successfully downloaded ones.