package com.github.loa.downloader.repository;

import com.github.loa.downloader.repository.domain.DocumentDatabaseEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DocumentRepository {

    @Insert("INSERT INTO document SET id = #{id}, url = #{url}, crawled_date = NOW(), crc = #{crc}, file_size = #{fileSize}, `status` = #{status}, crawler_version = #{crawlerVersion}")
    void insertDocument(DocumentDatabaseEntity tomeDatabaseEntity);

    @Select("SELECT * FROM document WHERE id = #{id}")
    DocumentDatabaseEntity findById(@Param("id") String id);

    @Select("SELECT * FROM document WHERE file_size = #{fileSize} AND crc = #{crc}")
    List<DocumentDatabaseEntity> findByCrcAndFileSize(@Param("crc") String crc, @Param("fileSize") long fileSize);
}
