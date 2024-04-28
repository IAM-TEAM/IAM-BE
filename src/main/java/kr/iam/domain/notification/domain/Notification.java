package kr.iam.domain.notification.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel.domain.Channel;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

}
