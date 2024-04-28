package kr.iam.domain.channel_advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.channel.domain.Channel;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAdvertisement {
    @Id @GeneratedValue
    @Column(name = "channel_advertisement_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;
}
