package com.moong.envers.approve.domain;

import com.moong.envers.approve.types.ApproveStatus;
import com.moong.envers.common.domain.BaseEntity;
import com.moong.envers.member.domain.Member;
import com.moong.envers.team.domain.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

@Audited
@Entity
@Getter
@AttributeOverrides( {
          @AttributeOverride(name = "createdDt", column = @Column(name = "approve_date"))
        , @AttributeOverride(name = "createdBy", column = @Column(name = "approve_member_name"))
})
@ToString(exclude = {"member", "team"}) @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Approve extends BaseEntity {

    @EmbeddedId
    private Id id;

    @MapsId("memberId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "fk_approve_member_id"))
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Member member;

    /**
     * @apiNote @EmbeddedId 복합키 사용을 명시했다.
     * @Embeddable 클래스에서 해당 필드명을 @MapsId([filed])에서 명시해준다.
     * 하이버네이트의 버전에 따라 다르겠지만, 해당 버전에선 @MapsId 생략이 가능하다.
     * @author moong
     * */
    @MapsId("teamId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "fk_approve_team_id"))
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApproveStatus status;

    @Builder(access = AccessLevel.PROTECTED)
    private Approve(Id id, Member member, Team team, ApproveStatus status) {
        this.id = id;
        this.member = member;
        this.team = team;
        this.status = status;
    }

    public static Approve newInstance(Member member, Team team) {
        return Approve.builder()
                .id(Id.builder()
                        .memberId(member.getId())
                        .teamId(team.getId())
                        .build())
                .member(member)
                .team(team)
                .status(ApproveStatus.WAIT)
                .build();
    }

    public Approve changeApproveStatus(ApproveStatus status) {
        this.status = status;
        return this;
    }

    @Embeddable
    @Getter
    @ToString
    @EqualsAndHashCode(of = { "member_id", "team_id" })
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Id implements Serializable {

        private static final long serialVersionUID = 4560214398288611577L;

        @Column(name = "member_id")
        private Long memberId;

        @Column(name = "team_id")
        private Long teamId;

        @Builder(access = AccessLevel.PRIVATE)
        private Id(Long memberId, Long teamId) {
            this.memberId = memberId;
            this.teamId = teamId;
        }
    }
}

