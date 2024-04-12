package kr.iam.domain.Notification.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.Channel.domain.Channel;

@Entity
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

}
