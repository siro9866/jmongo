package com.sil.jmongo.domain.file.repository;

import com.sil.jmongo.domain.file.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<File, String> {

    List<File> findByParentTypeAndParentId(String parentType, String parentId);
}
