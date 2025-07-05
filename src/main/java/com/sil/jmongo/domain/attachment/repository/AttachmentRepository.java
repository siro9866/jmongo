package com.sil.jmongo.domain.attachment.repository;

import com.sil.jmongo.domain.attachment.entity.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {

    List<Attachment> findByParentTypeAndParentId(String parentType, String parentId);
}
