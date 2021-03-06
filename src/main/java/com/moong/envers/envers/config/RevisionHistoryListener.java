package com.moong.envers.envers.config;

import com.moong.envers.envers.domain.RevisionHistory;
import com.moong.envers.envers.types.RevisionEventStatus;
import com.moong.envers.envers.types.RevisionTarget;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

import java.io.Serializable;


/**
 * @author moong
 * https://docs.jboss.org/hibernate/core/4.0/devguide/en-US/html/ch15.html
 * https://github.com/hibernate/hibernate-orm/blob/master/documentation/src/test/java/org/hibernate/userguide/envers/EntityTypeChangeAuditTrackingRevisionListenerTest.java
 */
@Slf4j
public class RevisionHistoryListener implements EntityTrackingRevisionListener {

    @Override
    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity) {
        log.info("EntityTrackingRevisionListener entityChanged start...");
        RevisionHistory revisionHistory = RevisionHistory.class.cast(revisionEntity);
        revisionHistory.addModifiedEntity(entityId,revisionType, RevisionTarget.of(entityClass), RevisionEventStatus.WAIT);
    }

    @Override
    public void newRevision(Object revisionEntity) {
        // todo : Spring Security를 사용하여 회원의 저장
    }
}
